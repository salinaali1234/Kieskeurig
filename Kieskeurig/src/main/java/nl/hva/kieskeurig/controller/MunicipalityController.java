package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.service.MunicipalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/municipalities")
public class MunicipalityController {
    private final MunicipalityService municipalityService;

    @Autowired // Tell springboot what controler to use
    public MunicipalityController(MunicipalityService service) {
        this.municipalityService = service;
    }


    @GetMapping("/all/{type}/{consistencyId}")
    public Map<String, Integer> getAllRegions(@PathVariable String type, @PathVariable Integer consistencyId) throws XMLStreamException, IOException {
        return municipalityService.getAllMunicipallities(type, consistencyId);
    }
}
