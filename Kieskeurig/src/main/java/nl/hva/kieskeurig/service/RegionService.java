package nl.hva.kieskeurig.service;
import nl.hva.kieskeurig.model.Municipality;
import nl.hva.kieskeurig.reader.RegionReader;
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
public class RegionService {
    private final ConstituencyRepo repo;
    private final List<Constituency> constituencies = new ArrayList<Constituency>();
    private final List<Municipality> municipalities = new ArrayList<Municipality>();
    private final View error;

    public void add(Constituency constituency) {
        constituencies.add(constituency);
    }
    public void add(Municipality municipality) {municipalities.add(municipality);}


    @Autowired
    public RegionService(ConstituencyRepo repo, View error) {this.repo = repo;
        this.error = error;
    }

    public List<Constituency> getAll() {return repo.findAll();}

    public boolean connectElectionDefinition(String type) throws XMLStreamException, IOException {
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

                            if (superiorRegionId != null) {
                                if (superiorRegionId == 0) {
                                    Constituency constituency = new Constituency(id, name);
                                    add(constituency);
                                } else {
                                    Municipality municipality = new Municipality(name, id, superiorRegionId);
                                    add(municipality);
                                }
                            }
                        }
                    }



            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public Map<String, Integer> getAllRegions(String type, Integer constistuencyId) throws XMLStreamException, IOException {
        if (connectElectionDefinition(type)) {
            Map<String, Integer> map = new HashMap<>();



            if (type.equals("municipalities")) {

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

            } else {
                for (Constituency constituency : constituencies) {
                    map.put(constituency.getName(), constituency.getId());
                }
            }
            return map;
        } else {
            return null;
        }
    }
}


