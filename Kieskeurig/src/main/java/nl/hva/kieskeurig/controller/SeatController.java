//package nl.hva.kieskeurig.controller;
//
//import nl.hva.kieskeurig.model.Candidate;
//import nl.hva.kieskeurig.model.Election;
//import nl.hva.kieskeurig.model.Party;
//import nl.hva.kieskeurig.service.SeatService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.xml.stream.XMLStreamException;
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/xml")
//public class SeatController {
//    private final SeatService service;
//
//    @Autowired
//    public SeatController(SeatService service) {
//        this.service = service;
//    }
//
//    @GetMapping
//    public List<Election> getAll() {
//        try {
//            return service.getAll();
//        } catch (IOException | XMLStreamException e) {
//            // Log the error
//            e.printStackTrace();
//            return List.of(); // give empty list in case of error
//        }
//    }
//
//    @GetMapping("/candidates/{partyId}")
//    public ResponseEntity<List<Candidate>> getCandidatesOfParty(@PathVariable int partyId) {
//        try {
//            List<Candidate> candidates = service.getCandidatesOfParty(partyId);
//            return ResponseEntity.ok(candidates);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//
//    @GetMapping("/parties/{partyId}")
//    public List<Party> getParty(@PathVariable int partyId) {
//        try {
//            return service.getParty(partyId)
//                    .map(List::of)
//                    .orElseGet(List::of);
//        } catch (IOException | XMLStreamException e) {
//            e.printStackTrace();
//            return List.of(); // list empty
//        }
//
//    }@GetMapping("/parties")
//    public List<Party> getAllParties() {
//        try {
//            return service.getAllParties();
//        } catch (IOException | XMLStreamException e) {
//            e.printStackTrace();
//            return List.of();
//        }
//    }
//
//}
////    @GetMapping("/parties/{partyId}")
////    public List<Party> getParty(@PathVariable int partyId) {
////        return service.getParty(partyId);
////    }
//
////    @PostMapping("/read/{folderName}")
////    public boolean readResults(@PathVariable String folderName) {
////        return service.readResults(folderName);
////    }
