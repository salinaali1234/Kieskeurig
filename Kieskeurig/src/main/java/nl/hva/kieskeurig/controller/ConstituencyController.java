package nl.hva.kieskeurig.controller;
import lombok.RequiredArgsConstructor;
import nl.hva.kieskeurig.model.Constituency;
import nl.hva.kieskeurig.service.ConstituencyService;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/constituencies")
@RequiredArgsConstructor
public class ConstituencyController {
    private final ConstituencyService service;

    // with in () is a dependency injection

    @GetMapping("/findAll")
    public List<Constituency> findAll() {
        return service.getAll();
    }

    @GetMapping("/all")
    public Map<String, Integer> getAllRegions() throws XMLStreamException, IOException {
        return service.getAllConsituencies();
    }

    @GetMapping("/all2")
    public List<Constituency> getAllConstituencies() {
        return service.getAllConstituenciesJPA();
    }

    @GetMapping("/Info/{ConstituencyName}")
    public Map<String, Integer> getInfoConstituency(@PathVariable String ConstituencyName) throws XMLStreamException, IOException {
        return service.getInfoConstituency(ConstituencyName);
    }





//    look at the names for the api-endpoint
}
