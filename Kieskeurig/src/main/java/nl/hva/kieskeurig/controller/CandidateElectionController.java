package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.CandidateForPartyInfo;
import nl.hva.kieskeurig.service.CandidatePartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateElectionController {

    private final CandidatePartyService service;

    @Autowired
    public CandidateElectionController(CandidatePartyService service) {
        this.service = service;
    }

    @GetMapping("/party/{partyId}")
    public ResponseEntity<List<CandidateForPartyInfo>> getCandidatesOfParty(@PathVariable int partyId) {
        try {
            return ResponseEntity.ok(service.getCandidatesOfParty(partyId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/elected")
    public ResponseEntity<List<CandidateForPartyInfo>> getElectedCandidates() {
        try {
            List<CandidateForPartyInfo> elected = service.getAllCandidates()
                    .stream().filter(CandidateForPartyInfo::isElected).toList();
            return ResponseEntity.ok(elected);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}