package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Object getVotesPerPartyByElectionByProvince(
            @PathVariable String electionId,
            @PathVariable String province,
            @RequestParam(required = false, defaultValue = "validVotes") String sort,
            @RequestParam(required = false, defaultValue = "false") boolean asc
    ) {
        return voteService.getVotesPerPartyByElectionByProvince(electionId, province, sort, asc);
    }
}
