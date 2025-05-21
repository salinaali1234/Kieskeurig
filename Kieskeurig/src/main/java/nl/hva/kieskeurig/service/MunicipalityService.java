package nl.hva.kieskeurig.service;
import nl.hva.kieskeurig.model.Municipality;
import nl.hva.kieskeurig.reader.RegionReader;
import nl.hva.kieskeurig.repository.ConstituencyRepo.ConstituencyRepository;
import nl.hva.kieskeurig.utils.xml.XMLParser;
import  nl.hva.kieskeurig.model.Constituency;
import nl.hva.kieskeurig.repository.ConstituencyRepo.ConstituencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MunicipalityService {

    private final List<Municipality> municipalities = new ArrayList<Municipality>();

    public void add(Municipality municipality) {municipalities.add(municipality);}


    @Autowired
    private ConstituencyRepository constituencyRepository;

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
                        Municipality municipality = new Municipality(name, id, superiorRegionId);
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
        System.out.println("runs municipalities");
         if (connectElectionDefinition()) {
            System.out.println("reading xml");
        } else {
            return null;
        }

        Map<String, Integer> map = new HashMap<>();
        if (constistuencyId == 0){

            for (Municipality municipality : municipalities){
                map.put(municipality.getName(), municipality.getId());
            }
        } else {
            for (Municipality municipality : municipalities) {
                if (constistuencyId.equals(municipality.getIdConstituency())){
                    map.put(municipality.getName(), municipality.getIdConstituency());
                }
            }
        }
        return map;
}}

