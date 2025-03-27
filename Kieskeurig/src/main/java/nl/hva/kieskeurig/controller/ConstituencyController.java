package nl.hva.kieskeurig.controller;
import nl.hva.kieskeurig.model.Constituency;
import nl.hva.kieskeurig.service.ConstituencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/constituency")
public class ConstituencyController {
    private final ConstituencyService service;

    public ConstituencyController(ConstituencyService service) {
        this.service = service;
    }



    @GetMapping("/findAll")
    public List<Constituency> findAll() {
        return service.getAll();}
}
