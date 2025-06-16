package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.service.MunicipalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import nl.hva.kieskeurig.model.Municipality;
import nl.hva.kieskeurig.model.VoteResult;
import nl.hva.kieskeurig.service.MunicipalityService;
import nl.hva.kieskeurig.utils.xml.DutchElectionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/municipalities")
public class MunicipalityController {
    private final MunicipalityService municipalityService;


    @Autowired // Tell springboot what controler to use
    public MunicipalityController(MunicipalityService service) {
        this.municipalityService = service;

    }


    @GetMapping("/all/{consistencyId}")
    public Map<String, Integer> getAllRegions(@PathVariable Integer consistencyId) throws XMLStreamException, IOException {
        return municipalityService.getAllMunicipallities( consistencyId);
    }

    @GetMapping("/Info/{MunicipalityName}/{year}")
    public Map<String, Integer> getInfoConstituency(@PathVariable String MunicipalityName, @PathVariable int year) throws XMLStreamException, IOException {
        return municipalityService.getInfoMunicipality(MunicipalityName, year);
    }


    @PostMapping("/load-all-votes")
    public ResponseEntity<String> loadAllVotes() {
        try {
            municipalityService.readAndStoreAllVotes();
            return ResponseEntity.ok("All votes loaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error loading votes: " + e.getMessage());
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> getMunicipalityById(@PathVariable String name) {
        try {
            VoteResult voteResult = municipalityService.getMunicipalityById(name);
            if (voteResult != null) {
                return ResponseEntity.ok(voteResult.getPartyName() + voteResult.getVotes());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

