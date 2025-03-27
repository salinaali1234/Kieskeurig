package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.repository.XMLRepo;
import nl.hva.kieskeurig.service.VotesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/xml")
public class XMLController {
    private final XMLRepo repo;
    private final VotesService votesService;

    public XMLController(XMLRepo repo, VotesService votesService) {
        this.repo = repo;
        this.votesService = votesService;
    }

    @GetMapping("/candidates/{partyId}")
    public List<Candidate> getCandidatesOfParty(@PathVariable int partyId) {
        return repo.getCandidatesOfParty(partyId);
    }

    @PostMapping("/read/{fileName}")
    public boolean getVotes(@PathVariable String fileName) {
        return votesService.readResults("C:\\Users\\safae\\IdeaProjects\\ciijeegoomii63\\Kieskeurig\\src\\main\\directory\\Verkiezingsuitslag Tweede Kamer 2023\\Totaaltelling_TK2023.eml.xml");
    }

    @GetMapping("/votes/parties")
    public Map<String, Integer> getVotesPerParty() {
        return votesService.getVotesPerParty();
    }
}
