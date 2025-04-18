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
    private final List<Vote> votes = new ArrayList<>();

    public void add(Vote vote) {
        votes.add(vote);
    }

    public boolean readResults(String fileName) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Verkiezingsuitslag_Tweede_Kamer_2023/" + fileName);

            if (inputStream == null) {
                System.err.println("Bestand niet gevonden: " + "Verkiezingsuitslag_Tweede_Kamer_2023/" + fileName);
                return false;
            }

            XMLParser xmlParser = new XMLParser(inputStream);
            VoteReader reader = new VoteReader(xmlParser);

            Map<String, Integer> partyVotes = reader.getValidVotes();

            for (Map.Entry<String, Integer> entry : partyVotes.entrySet()) {
                Vote vote = new Vote(entry.getKey(), entry.getValue());
                add(vote);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Integer> getVotesPerParty() {
        Map<String, Integer> partyVotes = new HashMap<>();
        for (Vote vote : votes) {
            partyVotes.put(vote.getPartyName(), partyVotes.getOrDefault(vote.getPartyName(), 0) + vote.getValidVotes());
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
                    readResults(fileName);
                }
            }
        }

        System.out.println(votes);

        for (Vote vote : votes) {
            partyVotes.put(vote.getPartyName(), partyVotes.getOrDefault(vote.getPartyName(), 0) + vote.getValidVotes());
        }

        votes.clear();
        return partyVotes;
    }
}
