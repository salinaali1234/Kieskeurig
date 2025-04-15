package nl.hva.kieskeurig.reader;

import nl.hva.kieskeurig.demo.DutchElectionTransformerForParties;
import nl.hva.kieskeurig.utils.PathUtils;
import nl.hva.kieskeurig.utils.xml.DutchElectionProcessor;
import nl.hva.kieskeurig.model.ElectionForParty;
import nl.hva.kieskeurig.repository.PartiesInfoRepo;

import org.springframework.stereotype.Service;
import javax.xml.stream.XMLStreamException;


import javax.imageio.IIOException;
import java.io.IOException;


@Service
public class DutchElectionReader {
    private PartiesInfoRepo xmlService;

    public DutchElectionReader(PartiesInfoRepo xmlService) {
        this.xmlService = xmlService;

    }

    public boolean readResults(String folderName, int Party) {
        System.out.println("Reading results");

        DutchElectionTransformerForParties transformer = new DutchElectionTransformerForParties();

        DutchElectionProcessor<ElectionForParty> electionProcessor = new DutchElectionProcessor<>(transformer);

        try{
            ElectionForParty election = electionProcessor.processResults("TK2023", PathUtils.getResourcePath("/%s".formatted(folderName)));

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
