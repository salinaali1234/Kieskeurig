package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.service.ProvinceVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * API handling everything related to parties.
 */
@RestController
@RequestMapping("/api/party")
public class PartyController {
    private final ProvinceVoteService provinceVoteService;

    @Autowired
    public PartyController(ProvinceVoteService voteService) {
        this.provinceVoteService = voteService;
    }

    @GetMapping("/{electionId}/{province}")
    public Object getVotesPerPartyByElectionByProvince(
            @PathVariable String electionId,
            @PathVariable String province,
            @RequestParam(required = false, defaultValue = "validVotes") String sort,
            @RequestParam(required = false, defaultValue = "false") boolean asc
    ) {
        return provinceVoteService.getVotesPerPartyByElectionByProvince(electionId, province, sort, asc);
    }
}
