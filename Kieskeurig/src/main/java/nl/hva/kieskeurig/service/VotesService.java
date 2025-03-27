package nl.hva.kieskeurig.service;

import nl.hva.ict.se.sm3.utils.xml.XMLParser;
import nl.hva.kieskeurig.model.Votes;
import nl.hva.kieskeurig.reader.DutchElectionReader;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class VotesService {
    private final List<Votes> votes = new ArrayList<>();

    public void add(Votes vote) {
        votes.add(vote);
    }


    public boolean readResults(String fileName) {
        try (InputStream inputStream = new FileInputStream(fileName)) {
            XMLParser xmlParser = new XMLParser(inputStream);
            DutchElectionReader reader = new DutchElectionReader(xmlParser);

            Map<String, Integer> partyVotes = reader.getValidVotes();


            for (Map.Entry<String, Integer> entry : partyVotes.entrySet()) {
                Votes vote = new Votes(entry.getKey(), entry.getValue());
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

        for (Votes vote : votes) {
            partyVotes.put(vote.getPartyName(), partyVotes.getOrDefault(vote.getPartyName(), 0) + vote.getValidVotes());
        }

        return partyVotes;
    }


}