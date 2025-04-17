package nl.hva.kieskeurig.controller;
import nl.hva.kieskeurig.model.Constituency;
import nl.hva.kieskeurig.service.ConstituencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/constituencies")
public class ConstituencyController {
    private final ConstituencyService service;

    @Autowired // Tell springboot what controler to use
    public ConstituencyController(ConstituencyService service) {
        this.service = service;
    }
    // with in () is a dependency injection

    @GetMapping("/findAll")
    public List<Constituency> findAll() {
        return service.getAll();}


    @GetMapping("/all/{type}/{consistencyId}")
    public Map<String, Integer> getAllConstituencies(@PathVariable String type, @PathVariable Integer consistencyId) throws XMLStreamException, IOException {
        //System.out.println("hiii"+ consistencyId);
        //System.out.println(service.getConstituencies(type, consistencyId));

        return service.getConstituencies(type, consistencyId);
    }



//    look at the names for the api-endpoint
}
