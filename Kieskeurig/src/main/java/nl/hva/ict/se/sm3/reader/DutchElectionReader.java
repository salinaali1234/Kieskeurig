package nl.hva.ict.se.sm3.reader;

import nl.hva.ict.se.sm3.demo.DutchElectionTransformer;
import nl.hva.ict.se.sm3.utils.PathUtils;
import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
import nl.hva.kieskeurig.model.Election;
import nl.hva.kieskeurig.repository.XMLRepo;

import org.springframework.stereotype.Service;
import javax.xml.stream.XMLStreamException;


import javax.imageio.IIOException;
import java.io.IOException;


@Service
public class DutchElectionReader {
    private XMLRepo xmlService;

    public DutchElectionReader(XMLRepo xmlService) {
        this.xmlService = xmlService;

    }

    public boolean readResults(String folderName, int Party) {
        System.out.println("Reading results");

        DutchElectionTransformer transformer = new DutchElectionTransformer();

        DutchElectionProcessor<Election> electionProcessor = new DutchElectionProcessor<>(transformer);

        try{
            Election election = electionProcessor.processResults("TK2023", PathUtils.getResourcePath("/%s".formatted(folderName)));

            xmlService.add(election);
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
