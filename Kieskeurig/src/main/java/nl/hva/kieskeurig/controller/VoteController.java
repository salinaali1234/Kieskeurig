package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.service.VoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST controller handles everything related to national votes.
 */
@RestController
@RequestMapping("/api/xml")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/votes/parties")
    public Map<String, Integer> getVotesPerParty(@RequestParam(defaultValue = "2023") String year) {
        return voteService.getResults(year);
    }
}
