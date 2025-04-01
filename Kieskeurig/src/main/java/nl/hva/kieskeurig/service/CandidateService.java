package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.repository.CandidateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public void addCandidate(Candidate candidate) {
        candidateRepo.addCandidate(candidate);
    }

    public List<Candidate> getCandidatesOfPartyByYear(int partyId, int year) {
        List<Candidate> candidateList = new ArrayList<>();

        return candidateList;
    }
}
