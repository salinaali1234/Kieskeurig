package nl.hva.kieskeurig.service;

//import nl.hva.ict.se.sm3.utils.xml.XMLParser;
import nl.hva.kieskeurig.enums.Province;
import nl.hva.kieskeurig.model.Vote;
import nl.hva.kieskeurig.reader.VoteReader;
import nl.hva.kieskeurig.utils.xml.XMLParser;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class VoteService {

    private final Map<String, List<Vote>> votesPerYear = new HashMap<>();

    public void add(String year, Vote vote) {
        votesPerYear.computeIfAbsent(year, key -> new ArrayList<>()).add(vote);
    }

    public boolean readResults(String folder, String fileName, String year) {

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


            for (Map.Entry<String, Integer> entry : partyVotes.entrySet()) {
                Vote vote = new Vote(entry.getKey(), entry.getValue());
                add(year, vote);
            }
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
            partyVotes.put(vote.getPartyName(),
                    partyVotes.getOrDefault(vote.getPartyName(), 0) + vote.getValidVotes());
        }

        votes.clear();
        return partyVotes;
    }

    public Map<String, Integer> getVotesPerPartyByElectionByProvince(String electionId, String province) {

        Map<String, Integer> partyVotes = new HashMap<>();
        String fileName;

        for (Province provinceName : Province.values()) {
            if (provinceName.getDisplayName().equalsIgnoreCase(province)) {
                for (String constituency : provinceName.getConstituencies()) {
                    fileName = "Kieskring tellingen/Telling_%s_kieskring_%s.eml.xml".formatted(
                            electionId.toUpperCase(),
                            constituency.replaceAll("'", "")
                    );

                    readResults("Verkiezingsuitslag_Tweede_Kamer_2023", fileName, electionId);
                }
            }
        }

        List<Vote> votesPerProvince = votesPerYear.getOrDefault(electionId, List.of());
        for (Vote vote : votesPerProvince) {
            partyVotes.put(
                    vote.getPartyName(),
                    partyVotes.getOrDefault(vote.getPartyName(), 0) + vote.getValidVotes()
            );
        }

        votesPerProvince.clear();
        return partyVotes;
    }
}