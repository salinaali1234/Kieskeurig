package nl.hva.kieskeurig.service;

import jakarta.transaction.Transactional;
import nl.hva.kieskeurig.enums.ProvinceEnum;
import nl.hva.kieskeurig.exception.NotFoundException;
import nl.hva.kieskeurig.model.Province;
import nl.hva.kieskeurig.repository.ProvinceRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ProvinceServiceTest {
    @Autowired
    private ProvinceRepo provinceRepo;

    @Autowired
    private ProvinceService provinceService;


    // addProvince()
    @Test
    void provinceService_addProvince_shouldSaveToRepository() {
        Province province = Province.builder()
                .name("Province")
                .build();

        provinceService.addProvince(province);

        Province returnedProvince = provinceService.getProvinceById(province.getId());

        assertNotNull(returnedProvince);
        assertEquals("Province", returnedProvince.getName());
        assertEquals(province.getId(), returnedProvince.getId());
    }

    @Test
    void provinceService_addProvince_nullShouldThrowException() {
        Province province = null;

        assertThrows(InvalidDataAccessApiUsageException.class, () -> provinceService.addProvince(province));
    }


    // getProvinceById()
    @Test
    void provinceService_getProvinceById_shouldReturnProvince_whenProvinceExists() {
        Province province = Province.builder()
                .name("Province")
                .build();

        provinceRepo.save(province);

        Province returnedProvince = provinceService.getProvinceById(province.getId());

        assertNotNull(returnedProvince);
        assertEquals("Province", returnedProvince.getName());
        assertEquals(province.getId(), returnedProvince.getId());
    }

    @Test
    void provinceService_getProvinceById_shouldThrowException_whenProvinceDoesNotExist() {
        int id = -1;

        assertThrows(NotFoundException.class, () -> provinceService.getProvinceById(id));
    }


    // getProvinceByName()
    @Test
    void provinceService_getProvinceByName_shouldReturnProvince_whenProvinceExists() {
        Province province = Province.builder()
                .name("Province")
                .build();

        provinceRepo.save(province);

        Province returnedProvince = provinceService.getProvinceByName(province.getName());

        assertNotNull(returnedProvince);
        assertEquals("Province", returnedProvince.getName());
    }

    @Test
    void provinceService_getProvinceByName_shouldThrowException_whenProvinceDoesNotExist() {
        String name = "Fake Province";

        assertThrows(NotFoundException.class, () -> provinceService.getProvinceByName(name));
    }


    // getAllProvinces()
    @Test
    void provinceService_getAllProvinces_shouldReturnAllProvinces_whenProvincesExist() {
        List<Province> provinces = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Province province = Province.builder()
                    .name("Province " + i)
                    .build();

            provinceRepo.save(province);
            provinces.add(province);
        }


        List<Province> returnedProvinces = provinceService.getAllProvinces();

        assertFalse(returnedProvinces.isEmpty());
        assertEquals(provinces.size(), returnedProvinces.size());
    }

    @Test
    void provinceService_getAllProvinces_shouldReturnEmptyList_whenProvincesDoNotExist() {
        List<Province> provinces = provinceService.getAllProvinces();

        assertTrue(provinces.isEmpty());
    }


    // isEmpty()
    @Test
    void provinceService_isEmpty_shouldReturnTrue_whenProvincesDoNotExist() {
        assertTrue(provinceService.isEmpty());
    }

    @Test
    void provinceService_isEmpty_shouldReturnFalse_whenProvincesDoExist() {
        Province province = Province.builder()
                .name("Province")
                .build();

        provinceRepo.save(province);

        assertFalse(provinceService.isEmpty());
    }


    // populateDatabase()
    @Test
    void provinceService_populateDatabase_shouldPopulateDatabase_withEveryProvince() {
        int totalProvinces = ProvinceEnum.values().length;

        provinceService.populateDatabase();
        List<Province> provinces = provinceService.getAllProvinces();

        assertEquals(totalProvinces, provinces.size());
    }
}