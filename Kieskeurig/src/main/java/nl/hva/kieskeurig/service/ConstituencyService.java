package nl.hva.kieskeurig.service;
import nl.hva.ict.se.sm3.demo.DutchElectionTransformer;
import nl.hva.ict.se.sm3.demo.Election;
import nl.hva.ict.se.sm3.utils.PathUtils;
import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
import nl.hva.ict.se.sm3.utils.xml.XMLParser;
import  nl.hva.kieskeurig.model.Constituency;
import nl.hva.kieskeurig.reader.ConstituencyReader;
import nl.hva.kieskeurig.repository.ConstituencyRepo.ConstituencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConstituencyService {
    private final ConstituencyRepo repo;
    private final List<Constituency> constituencies = new ArrayList<Constituency>();

    public void add(Constituency constituency) {
        constituencies.add(constituency);
    }


    @Autowired
    public ConstituencyService(ConstituencyRepo repo) {this.repo = repo;}

    public List<Constituency> getAll() {return repo.findAll();}


    public boolean readConstituencies() throws XMLStreamException, IOException {
                System.out.println("Processing files...");

                DutchElectionTransformer transformer = new DutchElectionTransformer();

                // And the election processor that traverses the folders and processes the XML-files.
                DutchElectionProcessor<Election> electionProcessor = new DutchElectionProcessor<>(transformer);
                try{
                    Election election= electionProcessor.processResults("TK2023", PathUtils.getResourcePath("/VerkiezingsuitslagTweedeKamer2023/Totaaltelling_TK2023.eml.xml"));
                System.out.println("All files are processed.\n");
                // Just print the 'results'
                if (election != null) {
                    System.out.println(election.toString());
                }
                System.out.println(election.data);
                return true;


            }catch (IOException | XMLStreamException | NullPointerException e) {
                System.out.println("Hij kon niets inlezen :(");

                return false;
            }
    }


    public boolean connectConstituencies() throws XMLStreamException, IOException {
        ClassPathResource resource = new ClassPathResource("VerkiezingsuitslagTweedeKamer2023/Totaaltelling_TK2023.eml.xml");
        System.out.println("getting everthing");
        try (InputStream inputStream = resource.getInputStream()) {
            System.out.println("Processing files...");
            System.out.println("inputstream" + inputStream.available());
            XMLParser xmlParser = new XMLParser(inputStream);
            ConstituencyReader reader = new ConstituencyReader(xmlParser);

            Map<String, String> constituencyMap = reader.getConstituencyMap();
            System.out.println(reader.getConstituencyMap());
            for (Map.Entry<String, String> entry : constituencyMap.entrySet()) {
                Constituency constituency = new Constituency(entry.getValue(),entry.getValue());
                add(constituency);
            }
            System.out.println("in the service"+ constituencyMap.toString());

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, Constituency> getConstituencies() {

        Map<String, Constituency> map = new HashMap<String, Constituency>();

        for (Constituency constituency : constituencies) {
            map.put(constituency.getId(), constituency);
            System.out.println();
        }
       return map;
 }

        }


