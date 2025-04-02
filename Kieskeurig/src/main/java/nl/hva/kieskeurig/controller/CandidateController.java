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

    @PostMapping()
    public void addCandidate() {
        Candidate candidate = new Candidate(
                1,
                "D",
                "Dilan",
                "",
                "Yeşilgöz",
                1,
                "VVD",
                9,
                "Amsterdam",
                "TK2023",
                "TK",
                "Tweede Kamer der Staten-Generaal 2023",
                "2023-11-22"
        );
        candidateService.addCandidate(candidate);
    }

    @GetMapping("/{electionId}/{partyName}")
    public List<Candidate> getCandidatesByElectionByParty(@PathVariable String electionId, @PathVariable String partyName) {
        return candidateService.getCandidatesByElectionByParty(electionId, partyName);
    }
}
