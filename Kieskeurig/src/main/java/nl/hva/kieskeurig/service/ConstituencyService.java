package nl.hva.kieskeurig.service;
import nl.hva.kieskeurig.demo.DutchElectionTransformer;
import nl.hva.kieskeurig.demo.Election;
import nl.hva.kieskeurig.model.Municipality;
import nl.hva.kieskeurig.reader.MunicipalitiesReader;
import nl.hva.kieskeurig.utils.PathUtils;
import nl.hva.kieskeurig.utils.xml.DutchElectionProcessor;
import nl.hva.kieskeurig.utils.xml.XMLParser;
import  nl.hva.kieskeurig.model.Constituency;
import nl.hva.kieskeurig.reader.ConstituencyReader;
import nl.hva.kieskeurig.repository.ConstituencyRepo.ConstituencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConstituencyService {
    private final ConstituencyRepo repo;
    private final List<Constituency> constituencies = new ArrayList<Constituency>();
    private final List<Municipality> municipalities = new ArrayList<Municipality>();
    private final View error;

    public void add(Constituency constituency) {
        constituencies.add(constituency);
    }
    public void add(Municipality municipality) {municipalities.add(municipality);}


    @Autowired
    public ConstituencyService(ConstituencyRepo repo, View error) {this.repo = repo;
        this.error = error;
    }

    public List<Constituency> getAll() {return repo.findAll();}

    public boolean connectElectionDefinition(String type) throws XMLStreamException, IOException {
        ClassPathResource resource = new ClassPathResource("Verkiezingsuitslag_Tweede_Kamer_2023/Verkiezingsdefinitie_TK2023.eml.xml");
        System.out.println("getting everthing");

        try (InputStream inputStream = resource.getInputStream()) {
            System.out.println("Processing files...");
            XMLParser xmlParser = new XMLParser(inputStream);

            if (type.equals("Constituencies")) {
                ConstituencyReader reader = new ConstituencyReader(xmlParser);

                Map<String, String> constituencyMap = reader.getConstituencyMap();
                for (Map.Entry<String, String> entry : constituencyMap.entrySet()) {
                    Constituency constituency = new Constituency(entry.getKey(), entry.getValue());
                    add(constituency);
                }
            }

            if (type.equals("municipalities")) {
                MunicipalitiesReader reader = new MunicipalitiesReader(xmlParser);

                Map<Integer, List<String>> municipalitiesMap = reader.getAllMunicipallies();
                for (Map.Entry<Integer, List<String>> entry : municipalitiesMap.entrySet()) {
                    Integer superiorRegionNumber = entry.getKey();
                    List<String> municipalities = entry.getValue();

                    for (String municipalityName : municipalities) {
                        Municipality municipality = new Municipality(
                                municipalityName,
                                superiorRegionNumber

                        );
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



    public Map<String, String> getConstituencies(String type, Integer constistuencyId) throws XMLStreamException, IOException {
        if (connectElectionDefinition(type)) {
            Map<String, String> map = new HashMap<>();
            System.out.println("constituecny id in the service"+constistuencyId);

            if (type.equals("Constituencies")) {
                for (Constituency constituency : constituencies) {
                    map.put(constituency.getName(), constituency.getId());
                }
            }
            if (type.equals("municipalities")) {

                if (constistuencyId == 0){

                    for (Municipality municipality : municipalities){
                        System.out.println(municipality.getName() + municipality.getIdConstituency());
                        map.put(municipality.getName(), String.valueOf(municipality.getIdConstituency()));
                    }
                } else {
                    for (Municipality municipality : municipalities) {
                        if (constistuencyId.equals(municipality.getIdConstituency())){
                            map.put(municipality.getName(), String.valueOf(municipality.getIdConstituency()));
                        } else {
                            System.out.println("couldn't find Municipality");
                        }
                    }
                }

            }

            return map;
        } else {
            return null;
        }
    }
}


