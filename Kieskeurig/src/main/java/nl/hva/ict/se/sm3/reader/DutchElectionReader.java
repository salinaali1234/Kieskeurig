package nl.hva.ict.se.sm3.reader;
import nl.hva.kieskeurig.demo.DutchElectionTransformer;
import nl.hva.kieskeurig.model.Election;
import nl.hva.kieskeurig.repository.XMLRepo;

import nl.hva.kieskeurig.utils.PathUtils;
import nl.hva.kieskeurig.utils.xml.DutchElectionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.xml.stream.XMLStreamException;


import javax.imageio.IIOException;
import java.io.IOException;


@Service
public class DutchElectionReader {

    @Autowired
    DutchElectionProcessor<Election> electionProcessor;

    private XMLRepo xmlService;

    public DutchElectionReader(XMLRepo xmlService) {
        this.xmlService = xmlService;

    }

    public boolean readResults(String folderName, int Party) {
        System.out.println("Reading results");

        try{
            Election election = electionProcessor.processResults("TK2023", PathUtils.getResourcePath("/%s".formatted(folderName)));

           // xmlService.add(election);
            System.out.printf("There are %d parties read. \n", election.getParties().size());
            return true;
        } catch (IIOException | XMLStreamException | NullPointerException e){
           System.err.println("failed to read results");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
