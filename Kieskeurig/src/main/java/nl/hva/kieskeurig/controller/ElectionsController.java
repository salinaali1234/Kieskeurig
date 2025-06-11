
package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.ElectionForParty;
import nl.hva.kieskeurig.service.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.XMLStreamException;

@RestController
@RequestMapping("/api/elections")
public class ElectionsController {

    private final ElectionService service;

    @Autowired
    public ElectionsController(ElectionService service) {
        this.service = service;
    }

    @GetMapping
    public List<ElectionForParty> getAllElections() {
        try {
            return service.getAll();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}