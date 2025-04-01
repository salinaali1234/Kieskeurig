package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.transformer.CandidateTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RegisterCandidateService {
    private final CandidateService candidateService;

    @Autowired
    public RegisterCandidateService(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    public void registerCandidate(Map<String, String> candidateData) {
        Candidate candidate = CandidateTransformer.transformCandidate(candidateData);
        candidateService.addCandidate(candidate);
    }
}
