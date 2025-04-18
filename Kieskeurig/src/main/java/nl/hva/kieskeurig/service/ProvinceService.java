package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.enums.Province;
import org.springframework.stereotype.Service;
import nl.hva.kieskeurig.controller.ProvinceController;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the logic of {@link ProvinceController}
 */
@Service
public class ProvinceService {
    public List<String> getProvinces() {
        ArrayList<String> provinces = new ArrayList<>();

        for (Province province : Province.values()) {
            provinces.add(province.getDisplayName());
        }

        return provinces;
    }
}
