package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.service.YearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API handling everything related to candidates.
 */
@RestController
@RequestMapping("/api/year")
public class YearController {
    private final YearService yearService;

    @Autowired
    public YearController(YearService yearService) {
        this.yearService = yearService;
    }

    @GetMapping()
    public List<String> getYears() {
        return yearService.getYears();
    }
}
