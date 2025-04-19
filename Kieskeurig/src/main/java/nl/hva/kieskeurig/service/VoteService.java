package nl.hva.kieskeurig.service;

//import nl.hva.ict.se.sm3.utils.xml.XMLParser;
import nl.hva.kieskeurig.model.Vote;
import nl.hva.kieskeurig.reader.NationalVotesReader;
import nl.hva.kieskeurig.utils.xml.XMLParser;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class VoteService {

    private final Map<String, List<Vote>> votesPerYear = new HashMap<>();
    private final Set<String> loadedYears = new HashSet<>();

    public void add(String year, Vote vote) {
        votesPerYear.computeIfAbsent(year, key -> new ArrayList<>()).add(vote);
    }

    public boolean readResults(String folder, String fileName, String year) {
        if (loadedYears.contains(year)) return true;

        try {
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream(folder + "/" + fileName);

            if (inputStream == null) {
                System.err.println("Bestand niet gevonden: " + folder + "/" + fileName);
                return false;
            }

            XMLParser xmlParser = new XMLParser(inputStream);
            NationalVotesReader reader = new NationalVotesReader(xmlParser);

            Map<String, Integer> partyVotes = reader.getValidVotes();

            for (Map.Entry<String, Integer> entry : partyVotes.entrySet()) {
                Vote vote = new Vote(entry.getKey(), entry.getValue());
                add(year, vote);
            }

            loadedYears.add(year);
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
        return partyVotes;
    }
}
