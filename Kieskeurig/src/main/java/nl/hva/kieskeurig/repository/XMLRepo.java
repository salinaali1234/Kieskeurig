package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Candidate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class XMLRepo {
    public List<Candidate> getCandidatesOfParty(int partyId) {
        // Test
        List<Candidate> candidates = new ArrayList<>();
        Candidate testCandidate = new Candidate(1, "D.", "Dilan", "Yeşilgöz", "female", "Amsterdam");

        candidates.add(testCandidate);
        return candidates;
    }
}
