package nl.hva.kieskeurig.service;
import nl.hva.kieskeurig.enums.ProvinceEnum;
import nl.hva.kieskeurig.exception.NotFoundException;
import nl.hva.kieskeurig.model.Municipality;
import nl.hva.kieskeurig.model.Province;
import nl.hva.kieskeurig.reader.RegionReader;
import nl.hva.kieskeurig.reader.VoteReader;
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
import java.util.*;

@Service
public class ConstituencyService {
    private final ConstituencyRepo repo;
    private final List<Constituency> constituencies = new ArrayList<>();
    private final View error;


    @Autowired
    public ConstituencyService(ConstituencyRepo repo, View error) {
        this.repo = repo;
        this.error = error;
    }

    @Autowired
    private ConstituencyRepository constituencyRepository;

    @Autowired
    private ProvinceService provinceService;

    // JPA repo functions
    public Constituency getById(Integer id) {
        return constituencyRepository.findById(id).orElseThrow(() -> new NotFoundException("Province not found"));
    }

    public boolean isEmpty() {
        return !constituencyRepository.existsBy();
    }

    // ArrayList function
    public void add(Constituency constituency) {
        constituencies.add(constituency);
    }

    // Repo class function
    public List<Constituency> getAll() {
        return repo.findAll();
    }

    // XML functions
    public Map<String, Integer> getInfoConstituency(String constituencyName) throws XMLStreamException, IOException {
        constituencyName = constituencyName.trim().replace(" ", "_");
        String path = String.format("Verkiezingsuitslag_Tweede_Kamer_2023/Kieskring tellingen/Telling_TK2023_kieskring_%s.eml.xml", constituencyName);
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

    public Map<String, Integer> getAllConsituencies() throws XMLStreamException, IOException {
        if (!constituencyRepository.findAll().isEmpty()) {
            System.out.println("Data already exists in the database, skipping XML import.");
        }else if (connectElectionDefinition()) {
            System.out.println("reading xml");
        } else {
            //this is if the connect Election gives back a false so something went wrong with reading
            return null;
        }

        Map<String, Integer> map = new HashMap<>();
        List<Constituency> allConstituencies = constituencyRepository.findAll();
        for (Constituency constituency : allConstituencies) {
            map.put(constituency.getName(), constituency.getId());
        }
        return map;
    }

    public boolean connectElectionDefinition() throws XMLStreamException, IOException {
        ClassPathResource resource = new ClassPathResource("Verkiezingsuitslag_Tweede_Kamer_2023/Verkiezingsdefinitie_TK2023.eml.xml");
        System.out.println("getting everything");

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

                    if (superiorRegionId != null && superiorRegionId == 0) {
                        Constituency constituency = new Constituency();

                        // Set province
                        for (ProvinceEnum provinceEnum : ProvinceEnum.values()) {
                            if (Arrays.stream(provinceEnum.getConstituencies()).toList().contains(name)) {
                                Province province = provinceService.getProvinceByName(provinceEnum.getDisplayName());
                                constituency.setProvince(province);
                                break;
                            }
                        }

                        constituency.setId(id);
                        constituency.setName(name);
                        Constituency saved = constituencyRepository.save(constituency);
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
}


