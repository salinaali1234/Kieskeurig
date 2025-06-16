package nl.hva.kieskeurig.controller;

import lombok.AllArgsConstructor;
import nl.hva.kieskeurig.model.Province;
import nl.hva.kieskeurig.service.ProvinceService;
import nl.hva.kieskeurig.service.ProvinceVoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * API handling everything related to provinces.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {
    private final ProvinceVoteService provinceVoteService;
    private final ProvinceService provinceService;

    @GetMapping
    public List<String> getProvinces() {
        return provinceVoteService.getProvinces();
    }

    @GetMapping("/all")
    public List<Province> getAllProvinces() {
        return provinceService.getAllProvinces();
    }

    @GetMapping("/{id}")
    public Province getProvinceById(@PathVariable int id) {
        return provinceService.getProvinceById(id);
    }

    @PostMapping
    public void addProvince(@RequestBody Province province) {
        provinceService.addProvince(province);
    }


    @GetMapping("/{province}/votes")
    public Map<String, Object> getTotalVotesByProvince(@PathVariable String province, @RequestParam String electionId) {
        return provinceVoteService.getTotalVotesByProvince(electionId, province);
    }

}

