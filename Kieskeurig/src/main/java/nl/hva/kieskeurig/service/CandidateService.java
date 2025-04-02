package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.controller.CandidateController;
import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.repository.CandidateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles the logic of {@link CandidateController}
 */
@Service
public class CandidateService {
    private final CandidateRepo candidateRepo;

    @Autowired
    public CandidateService(CandidateRepo candidateRepo) {
        this.candidateRepo = candidateRepo;
    }

    public List<Candidate> getCandidates() {
        return candidateRepo.getCandidates();
    }

    public List<Candidate> getCandidatesByElectionByParty(String electionId, String partyName) {
        List<Candidate> candidateList = candidateRepo.getCandidates();
        
        return candidateList.stream().filter(candidate ->
            candidate.getElectionIdentifier().equalsIgnoreCase(electionId) &&
            candidate.getRegisteredName().equalsIgnoreCase(partyName)
        ).toList();
    }
}
