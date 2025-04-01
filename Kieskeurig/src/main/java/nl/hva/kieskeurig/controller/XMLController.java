package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.Election;
import nl.hva.kieskeurig.model.Party;
import nl.hva.kieskeurig.service.XMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/xml")
public class XMLController {
    private final XMLService service;

    @Autowired
    public XMLController(XMLService service) {
        this.service = service;
    }

    @GetMapping
    public List<Election> getAll() {
        return service.getAll();
    }

    @GetMapping("/candidates/{partyId}")
    public List<Candidate> getCandidatesOfParty(@PathVariable int partyId) {
        return service.getCandidatesOfParty(partyId);
    }

    @GetMapping("/parties/{partyId}")
    public List<Party> getParty(@PathVariable int partyId) {
        return service.getParty(partyId);
    }

//    @PostMapping("/read/{folderName}")
//    public boolean readResults(@PathVariable String folderName) {
//        return service.readResults(folderName);
//    }
}