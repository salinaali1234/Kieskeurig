package nl.hva.kieskeurig.repository;

//import nl.hva.ict.se.sm3.reader.DutchElectionReader;
import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.Election;
import nl.hva.kieskeurig.model.Party;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class XMLRepo {

//    public void add(Election election) {
//        election.add(election);
//    }

    public List<Candidate> getCandidatesOfParty(int partyId) {
        // Test
        List<Candidate> candidates = new ArrayList<>();
        Candidate testCandidate = new Candidate(1,1, "D.", "Dilan", "Yeşilgöz", "female", "Amsterdam");

        candidates.add(testCandidate);
        return candidates;
    }

    public List<Party> getParty(int partyId) {
        // Test
        List<Party> parties = new ArrayList<>();
        Party testParty = new Party(1, "PVV");

        parties.add(testParty);
        return parties;
    }

    public List<Election> getAll() {
        return List.of();
    }

//    public boolean readResults(String folderName) {
//        DutchElectionReader reader = new DutchElectionReader(this); // Correcte manier om een instantie van DutchElectionReader aan te maken
//        return reader.readResults(folderName); // Alleen folderName doorgeven, zonder 'party'
//    }
}