package nl.hva.kieskeurig.service;
import jakarta.transaction.Transactional;
import nl.hva.kieskeurig.exception.NotFoundException;
import nl.hva.kieskeurig.model.Municipality;
import nl.hva.kieskeurig.reader.RegionReader;
import nl.hva.kieskeurig.repository.ConstituencyRepo.ConstituencyRepository;
import nl.hva.kieskeurig.repository.MunicipalityRepo;
import nl.hva.kieskeurig.utils.xml.XMLParser;
import  nl.hva.kieskeurig.model.Constituency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MunicipalityService {
    private final List<Municipality> municipalities = new ArrayList<>();

    private final MunicipalityRepo municipalityRepo;
    private final ConstituencyService constituencyService;

    @Autowired
    public MunicipalityService(MunicipalityRepo municipalityRepo, ConstituencyService constituencyService) {
        this.municipalityRepo = municipalityRepo;
        this.constituencyService = constituencyService;
    }

    // JPA repo functions
    public Municipality getById(int id) {
        return municipalityRepo.findById(id).orElseThrow(() -> new NotFoundException("Municipality with id " + id + " not found"));
    }

    public List<Municipality> getAll() {
        return municipalityRepo.findAll();
    }

    public void addMunicipality(Municipality municipality) {
        municipalityRepo.save(municipality);
    }

    public boolean isEmpty() {
        return !municipalityRepo.existsBy();
    }

    // ArrayList function
    public void add(Municipality municipality) {
        municipalities.add(municipality);
    }

    // XML functions
    @Transactional
    public boolean connectElectionDefinition() throws XMLStreamException, IOException {
        ClassPathResource resource = new ClassPathResource("Verkiezingsuitslag_Tweede_Kamer_2023/Verkiezingsdefinitie_TK2023.eml.xml");
        System.out.println("getting everthing");

        try (InputStream inputStream = resource.getInputStream()) {
            System.out.println("Processing files...");
            XMLParser xmlParser = new XMLParser(inputStream);
            RegionReader reader = new RegionReader(xmlParser);

            Map<Integer, Map<String, Integer>> municipalitiesMap = reader.getAllRegions();

            for (Map.Entry<Integer, Map<String, Integer>> outerEntry : municipalitiesMap.entrySet()) {
                Integer id = outerEntry.getKey();
                Map<String, Integer> innerMap = outerEntry.getValue();

                for (Map.Entry<String, Integer> innerEntry : innerMap.entrySet()) {
                    String name = innerEntry.getKey();
                    Integer superiorRegionId = innerEntry.getValue();

                    if (superiorRegionId != null && superiorRegionId != 0) {
                        Constituency constituency = null;
                        boolean isDordrecht = superiorRegionId.equals(14);
                        if (!isDordrecht) constituency = constituencyService.getConstituencyById(superiorRegionId);

                        Municipality municipality = new Municipality(id, name, constituency);
                        addMunicipality(municipality);
                        add(municipality);
                    }
                }
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Integer> getAllMunicipallities(Integer constistuencyId) throws XMLStreamException, IOException {
//        System.out.println("runs municipalities");
//        if (connectElectionDefinition()) {
//            System.out.println("reading xml");
//        } else {
//            return null;
//        }

        Map<String, Integer> map = new HashMap<>();
        if (constistuencyId == 0){

            for (Municipality municipality : municipalities){
                map.put(municipality.getName(), municipality.getId());
            }
        } else {
            for (Municipality municipality : municipalities) {
                if (constistuencyId.equals(municipality.getConstituency().getId())){
                    map.put(municipality.getName(), municipality.getConstituency().getId());
                }
            }
        }
        return map;
}}

