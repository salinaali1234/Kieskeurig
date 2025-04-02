package nl.hva.kieskeurig.controller;
import nl.hva.kieskeurig.model.Constituency;
import nl.hva.kieskeurig.service.ConstituencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("/readConstituencies")
    public boolean readConstituencies() throws XMLStreamException, IOException {
        return service.readConstituencies();
    }

    @GetMapping("/connect")
    public boolean connectConstituencies(Constituency constituency) throws XMLStreamException, IOException {
        return service.connectConstituencies();
    }

    public Map<Integer, String> getAllConstituencies() {
        return service.getALlConstituenciesXML();
    }


//    look at the names for the api-endpoint
}
