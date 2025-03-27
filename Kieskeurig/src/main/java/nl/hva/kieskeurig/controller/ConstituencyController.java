package nl.hva.kieskeurig.controller;
import nl.hva.kieskeurig.model.Constituency;
import nl.hva.kieskeurig.service.ConstituencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/constituencies")
public class ConstituencyController {
    private final ConstituencyService service;

    @Autowired // Tell springboot what controler to use
    public ConstituencyController(ConstituencyService service) {
        this.service = service;
    }
    // with in () is a dependency injection

    @GetMapping
    public List<Constituency> findAll() {
        return service.getAll();}

//    look at the names for the api-endpoint
}
