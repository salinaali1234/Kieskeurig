package nl.hva.kieskeurig.service;

//import nl.hva.ict.se.sm3.utils.xml.XMLParser;
import nl.hva.kieskeurig.enums.Province;
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

    public VoteService(NationalVotesRepo voteRepo) {
        this.voteRepo = voteRepo;
    }

    private final Map<String, List<Vote>> votesPerYear = new HashMap<>();

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
                    partyVotes.getOrDefault(vote.getPartyName(),0) + vote.getValidVotes()
            );
        }

//        votes.clear();
        return partyVotes;
    }

    public Object getVotesPerPartyByElectionByProvince(String electionId, String province, String sort, boolean asc) {
        if (sort == null) sort = "";
        String fileName;

        // Read the results for every constituency for the selected province
        for (Province provinceName : Province.values()) {
            if (provinceName.getDisplayName().equalsIgnoreCase(province)) {
                for (String constituency : provinceName.getConstituencies()) {
                    System.out.println(constituency);
                    fileName = "Kieskring tellingen/Telling_%s_kieskring_%s.eml.xml".formatted(
                            electionId.toUpperCase(),
                            constituency.replaceAll("'", "")
                    );

                    readResults("Verkiezingsuitslag_Tweede_Kamer_2023", fileName, electionId);
                }
            }
        }

        List<Vote> totalPartyVotesList = getVotes(electionId);
        return sortVotes(totalPartyVotesList, sort, asc);
    }

    /**
     * Gets the total amount of valid votes each party got and returns it in a {@link List<Vote>} containing objects with the {@link Vote} class.
     * @param electionId Type {@link String}
     * @return {@link List<Vote>}
     */
    private List<Vote> getVotes(String electionId) {
        // Calculate the total votes for each party and put it in a map to get rid of duplicates
        List<Vote> votes = votesPerYear.getOrDefault(electionId, List.of());
        Map<String, Integer> totalPartyVotesMap = new HashMap<>();
        for (Vote vote : votes) {
            totalPartyVotesMap.put(
                    vote.getPartyName(),
                    totalPartyVotesMap.getOrDefault(vote.getPartyName(), 0) + vote.getValidVotes()
            );
        }

        // Turn the map into a list containing the total votes for each party
        List<Vote> totalPartyVotesList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : totalPartyVotesMap.entrySet()) {
            Vote partyVoteResult = new Vote(entry.getKey(), entry.getValue(), electionId);
            totalPartyVotesList.add(partyVoteResult);
        }


        votes.clear();

        return totalPartyVotesList;
    }

    /**
     * Takes a {@link List<Vote>} containing objects with the {@link Vote} class and will return a sorted {@link List<Vote>} containing objects of the same class based on the given parameters.
     * @param votes Type {@link List<Vote>}
     * @param sort Type {@link String}
     * @param asc Type {@link Boolean}
     * @return {@link List<Vote>}
     */
    private List<Vote> sortVotes(List<Vote> votes, String sort, boolean asc) {
        if (sort.equals("partyName")) {
            votes.sort(Comparator.comparing(Vote::getPartyName));
        } else {
            votes.sort(Comparator.comparing(Vote::getValidVotes));
        }

        if (!asc) Collections.reverse(votes);

        return votes;
    }
}