package nl.hva.kieskeurig.service;
import nl.hva.kieskeurig.model.Municipality;
import nl.hva.kieskeurig.model.VoteResult;
import nl.hva.kieskeurig.reader.RegionReader;
import nl.hva.kieskeurig.reader.VoteReader;
import nl.hva.kieskeurig.repository.ConstituencyRepo.ConstituencyRepository;
import nl.hva.kieskeurig.repository.MunicipalityRepo;
import nl.hva.kieskeurig.repository.VoteResultRepo;
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
import java.util.*;

@Service
public class MunicipalityService {

    private final List<Municipality> municipalities = new ArrayList<Municipality>();
    @Autowired
    private MunicipalityRepo municipalityRepo;
    @Autowired
    private VoteResultRepo voteResultRepo;

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
                        Municipality municipality = new Municipality(id, name, superiorRegionId);
                        municipality.setId(id);
                        municipality.setName(name);
                        municipality.setConstituency(superiorRegionId);
                        Municipality saved = municipalityRepo.save(municipality);
                        add(saved);
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
        List<Municipality> sourceMunicipalities;

        if (!municipalityRepo.findAll().isEmpty()) {
            System.out.println("Data exists in database");
            sourceMunicipalities = municipalityRepo.findAll();
        } else if (connectElectionDefinition()) {
            System.out.println("reading xml");
            sourceMunicipalities = municipalities; // assuming connectElectionDefinition populates this
        } else {
            return null;
        }

        Map<String, Integer> map = new HashMap<>();
        if (constistuencyId == 0) {
            for (Municipality municipality : sourceMunicipalities) {
                map.put(municipality.getName(), municipality.getId());
            }
        } else {
            for (Municipality municipality : sourceMunicipalities) {
                if (constistuencyId.equals(municipality.getConstituency())) {
                    System.out.println("Matched municipality: " + municipality.getName());
                    map.put(municipality.getName(), municipality.getId());
                }
            }
        }

        return map;
    }

    public Map<String, Integer> getInfoMunicipality(String municipalityName, Integer year) throws XMLStreamException, IOException {
        municipalityName = municipalityName.trim().replace(" ", "_");
        String path = String.format("Verkiezingsuitslag_Tweede_Kamer_%s/Gemeente tellingen/Telling_TK%s_gemeente_%s.eml.xml",year,year, municipalityName);
        ClassPathResource resource = new ClassPathResource(path);
        System.out.println("getting everthing");
        Map<String, Integer> map = new HashMap<>();

        try (InputStream inputStream = resource.getInputStream()) {
            System.out.println("Processing files...");
            XMLParser xmlParser = new XMLParser(inputStream);
            VoteReader reader = new VoteReader(xmlParser);

            map= reader.getValidVotes();
        }
        return map; }


    public void readAndStoreAllVotes() throws XMLStreamException, IOException {
        if (municipalities.isEmpty()) {
            connectElectionDefinition();
        }

        for (Municipality municipality : municipalities) {
            try {
                Map<String, Integer> votes = getInfoMunicipality(municipality.getName(), 2023);
                for (Map.Entry<String, Integer> entry : votes.entrySet()) {
                    VoteResult result = new VoteResult();
                    result.setPartyName(entry.getKey());
                    result.setVotes(entry.getValue());
                    result.setMunicipality(municipality);
                    voteResultRepo.save(result);
                }
            } catch (IOException e) {
                System.err.println("Could not read file for " + municipality.getName());
            }
        }
    }



    public VoteResult getMunicipalityById(String name) throws XMLStreamException, IOException {
        // First check if data exists in the database
        List<VoteResult> dbMunicipalities = voteResultRepo.findAll();
        System.out.println(dbMunicipalities.getFirst().getMunicipality().getName());

        if (!dbMunicipalities.isEmpty()) {
            System.out.println("Municipalities loaded from DB.");
        } else if (connectElectionDefinition()) {
            System.out.println("Municipalities loaded from XML.");
        } else {
            return null; // No data found from either source
        }

        // Now look for the municipality by ID
        for (VoteResult municipality : dbMunicipalities) {
            System.out.println(municipality.getMunicipality().getName());
            if (Objects.equals(municipality.getMunicipality().getName(), name)) {
                System.out.println("Found: " + municipality.getMunicipality().getName() + municipality.getPartyName());
                return municipality;
            }
        }

        // Not found
        System.out.println("Municipality with ID " + name + " not found.");
        return null;
    }
}
