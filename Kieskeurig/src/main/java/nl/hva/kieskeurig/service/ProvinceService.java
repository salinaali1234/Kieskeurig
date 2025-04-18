package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.enums.Province;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
