package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.controller.CandidateController;
import nl.hva.kieskeurig.dto.CandidateDTO;
import nl.hva.kieskeurig.mapper.CandidateDTOMapper;
import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.CandidateForPartyInfo;
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

    public void addCandidate(Candidate candidate) {
        candidateRepo.addCandidate(candidate);
    }

    public List<CandidateDTO> getCandidatesByElectionByParty(String electionId, String partyName) {
        List<Candidate> candidateList = candidateRepo.getCandidates();

        // Gets the candidates from the specified party from the specified election, maps Candidate to CandidateDTO and filters out duplicates
        return candidateList.stream()
            .filter(candidate ->
                candidate.getElectionIdentifier().equalsIgnoreCase(electionId) &&
                candidate.getRegisteredName().equalsIgnoreCase(partyName)
            )
            .map(new CandidateDTOMapper())
            .distinct()
            .toList();
    }
}
