package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API handling everything related to provinces.
 */
@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {
    private final ProvinceService provinceService;

    @Autowired
    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @GetMapping
    public List<String> getProvinces() {
        return provinceService.getProvinces();
    }


    @GetMapping("/{province}/votes")
    public Map<String, Object> getTotalVotesByProvince(@PathVariable String province, @RequestParam String electionId) {
        return provinceService.getTotalVotesByProvince(electionId, province);
    }

}

