package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.repository.XMLRepo;
import nl.hva.kieskeurig.service.VoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/xml")
public class XMLController {
    private final XMLRepo repo;

    public XMLController(XMLRepo repo, VoteService voteService) {
        this.repo = repo;
    }

    @GetMapping("/candidates/{partyId}")
    public List<Candidate> getCandidatesOfParty(@PathVariable int partyId) {
        return repo.getCandidatesOfParty(partyId);
    }

}
