package nl.hva.kieskeurig.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.hva.kieskeurig.enums.ProvinceEnum;
import nl.hva.kieskeurig.exception.NotFoundException;
import nl.hva.kieskeurig.model.Province;
import nl.hva.kieskeurig.repository.ProvinceRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProvinceService {
    private ProvinceRepo provinceRepo;

    public void addProvince(Province province) {
        provinceRepo.save(province);
    }

    public Province getProvinceById(int id) {
        return provinceRepo.findById(id).orElseThrow(() -> new NotFoundException("Province with id " + id + " not found"));
    }

    public Province getProvinceByName(String name) {
        return provinceRepo.findByName(name).orElseThrow(() -> new NotFoundException("Province with name " + name + " not found"));
    }

    public List<Province> getAllProvinces() {
        return provinceRepo.findAll();
    }

    public boolean isEmpty() {
        return !provinceRepo.existsBy();
    }

    @Transactional
    public void populateDatabase() {
        for (ProvinceEnum province : ProvinceEnum.values()) {
            Province newProvince = new Province(province.getDisplayName());
            this.addProvince(newProvince);
        }
    }
}
