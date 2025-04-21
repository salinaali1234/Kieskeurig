package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.CandidateForPartyInfo;
import nl.hva.kieskeurig.model.ElectionForParty;
import nl.hva.kieskeurig.model.PartyWithInfo;
import nl.hva.kieskeurig.service.PartiesInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/partiesInfo")
public class PartiesInfoController {
    private final PartiesInfoService service;

    @Autowired
    public PartiesInfoController(PartiesInfoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ElectionForParty> getAll() {
        try {
            return service.getAll();
        } catch (IOException | XMLStreamException e) {
            // Log the error
            e.printStackTrace();
            return List.of(); // give empty list in case of error
        }
    }

    @GetMapping("/candidates/{partyId}")
    public ResponseEntity<List<CandidateForPartyInfo>> getCandidatesOfParty(@PathVariable int partyId) {
        try {
            List<CandidateForPartyInfo> candidates = service.getCandidatesOfParty(partyId);
            return ResponseEntity.ok(candidates);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/parties/{partyId}")
    public List<PartyWithInfo> getParty(@PathVariable int partyId) {
        try {
            return service.getParty(partyId)
                    .map(List::of)
                    .orElseGet(List::of);
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
            return List.of(); // list empty
        }

    }@GetMapping("/parties")
    public List<PartyWithInfo> getAllParties() {
        try {
            return service.getAllParties();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
            return List.of();
        }
    }
//    @GetMapping("/candidates/resultaat")
//    public ResponseEntity<List<CandidateForPartyInfo>> getElectedCandidatesFromResultaatFile() {
//        try {
//            List<CandidateForPartyInfo> electedCandidates = service.getElectedCandidatesFromResultaatFile();
//            return ResponseEntity.ok(electedCandidates);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//    }

}
//    @GetMapping("/parties/{partyId}")
//    public List<Party> getParty(@PathVariable int partyId) {
//        return service.getParty(partyId);
//    }

//    @PostMapping("/read/{folderName}")
//    public boolean readResults(@PathVariable String folderName) {
//        return service.readResults(folderName);
//    }
