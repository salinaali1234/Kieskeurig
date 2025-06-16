package nl.hva.kieskeurig.service;

//import nl.hva.ict.se.sm3.utils.xml.XMLParser;
import nl.hva.kieskeurig.model.Vote;
import nl.hva.kieskeurig.reader.VoteReader;
import nl.hva.kieskeurig.repository.NationalVotesRepo;
import nl.hva.kieskeurig.utils.xml.XMLParser;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class VoteService {
    private final NationalVotesRepo voteRepo;
    private final Map<String, List<Vote>> votesPerYear = new HashMap<>();

    public VoteService(NationalVotesRepo voteRepo) {
        this.voteRepo = voteRepo;
    }

    public Map<String, Integer> getResults(String year) {
//        votesPerYear.remove(year);
        String folder = "Verkiezingsuitslag_Tweede_Kamer_" + year;
        String fileName = getFileNameForYear(year);

        boolean success = readResults(folder, fileName, year);
        if (!success) {
            throw new RuntimeException("Kon de resultaten niet inlezen voor jaar " + year);
        }

        return getVotesPerParty(year);
    }

    private String getFileNameForYear(String year) {
        return switch (year) {
            case "2021" -> "Totaaltelling_TK2021.eml.xml";
            case "2023" -> "Totaaltelling_TK2023.eml.xml";
            default -> throw new IllegalArgumentException("Ongeldig jaar opgegeven: " + year);
        };
    }

    public void add(String year, Vote vote) {
        votesPerYear.computeIfAbsent(year, key -> new ArrayList<>()).add(vote);
    }

    public boolean readResults(String folder, String fileName, String year) {
        if (!voteRepo.findAllByYear(year).isEmpty()) {
            votesPerYear.put(year, voteRepo.findAllByYear(year));
            return true;
        }

        try {
            System.out.println("Trying to load: " + folder + "/" + fileName);

            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream(folder + "/" + fileName);

            if (inputStream == null) {
                return false;
            }

            XMLParser xmlParser = new XMLParser(inputStream);
            VoteReader reader = new VoteReader(xmlParser);

            Map<String, Integer> partyVotes = reader.getValidVotes();

            List<Vote> newVotes = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : partyVotes.entrySet()) {
                String partyName = entry.getKey();
                int votes = entry.getValue();

                Vote vote = new Vote(partyName, votes, year);
                voteRepo.save(vote);
                newVotes.add(vote);
            }

            votesPerYear.put(year, newVotes);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Integer> getVotesPerParty(String year) {
        Map<String, Integer> partyVotes = new HashMap<>();
        List<Vote> votes = votesPerYear.getOrDefault(year, List.of());


        for (Vote vote : votes) {
            partyVotes.put(
                    vote.getPartyName(),
                    partyVotes.getOrDefault(vote.getPartyName(), 0) + vote.getValidVotes()
            );
        }

//        votes.clear();
        return partyVotes;
    }
}