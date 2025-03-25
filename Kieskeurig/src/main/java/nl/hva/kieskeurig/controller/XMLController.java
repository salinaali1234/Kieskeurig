package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.Party;
import nl.hva.kieskeurig.repository.XMLRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/xml")
public class XMLController {
    private final XMLRepo repo;

    public XMLController(XMLRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/candidates/{partyId}")
    public List<Candidate> getCandidatesOfParty(@PathVariable int partyId) {
        return repo.getCandidatesOfParty(partyId);
    }

    @GetMapping("electionresults/parties/{party_id}")
    public List<Party> getParty(@PathVariable int party_id) {
        return repo.getParty(party_id);
    }
}
