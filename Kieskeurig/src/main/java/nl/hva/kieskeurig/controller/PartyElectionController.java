package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.PartyWithInfo;
import nl.hva.kieskeurig.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/parties")
public class PartyElectionController {

    private final PartyService service;

    @Autowired
    public PartyElectionController(PartyService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PartyWithInfo>> getAllParties(@RequestParam(defaultValue = "seats-desc") String sort) {
        try {
            return ResponseEntity.ok(service.getAllParties(sort));
        } catch (IOException | XMLStreamException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{partyId}")
    public List<PartyWithInfo> getParty(@PathVariable int partyId) {
        try {
            return service.getParty(partyId).map(List::of).orElseGet(List::of);
        } catch (IOException | XMLStreamException e) {
            return List.of();
        }
    }
}
