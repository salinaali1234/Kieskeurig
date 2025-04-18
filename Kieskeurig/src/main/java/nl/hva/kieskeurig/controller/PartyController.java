package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * API handling everything related to parties.
 */
@RestController
@RequestMapping("/api/party")
public class PartyController {
    private final VoteService voteService;

    @Autowired
    public PartyController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/{electionId}/{province}")
    public Map<String, Integer> getVotesPerPartyByElectionByProvince(@PathVariable String electionId, @PathVariable String province) {
        return voteService.getVotesPerPartyByElectionByProvince(electionId, province);
    }
}
