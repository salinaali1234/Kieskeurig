package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.repository.XMLRepo;
import nl.hva.kieskeurig.service.VoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/xml")
public class VoteController {

    private final VoteService voteService;

    public VoteController(XMLRepo repo, VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/votes/parties")
    public Map<String, Integer> getVotesPerParty() {
        voteService.readResults("Totaaltelling_TK2023.eml.xml");
        return voteService.getVotesPerParty();
    }
}
