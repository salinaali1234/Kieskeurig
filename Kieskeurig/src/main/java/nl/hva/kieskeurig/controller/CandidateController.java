package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API handling everything related to candidates.
 */
@RestController
@RequestMapping("/api/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping()
    public List<Candidate> getCandidates() {
        return candidateService.getCandidates();
    }

    @GetMapping("/{electionId}/{partyName}")
    public List<Candidate> getCandidatesByElectionByParty(@PathVariable String electionId, @PathVariable String partyName) {
        return candidateService.getCandidatesByElectionByParty(electionId, partyName);
    }
}
