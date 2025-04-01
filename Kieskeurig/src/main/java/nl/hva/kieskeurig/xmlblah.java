package nl.hva.kieskeurig;

import nl.hva.ict.se.sm3.demo.DutchElectionTransformer;
import nl.hva.ict.se.sm3.demo.Election;
import nl.hva.ict.se.sm3.utils.PathUtils;
import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

@Component
public class xmlblah implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        xmlProcessing();
    }

    /**
     * This function processes the XML using the code from Nico's DutchElectionProcessorDemo
     * @throws IOException
     * @throws XMLStreamException
     */
    private static void xmlProcessing() throws IOException, XMLStreamException {
        System.out.println("Processing files...");

        // We need a Transformer that has knowledge of your classes.
        DutchElectionTransformer creator = new DutchElectionTransformer();

        // And the election processor that traverses the folders and processes the XML-files.
        DutchElectionProcessor<Election> electionProcessor = new DutchElectionProcessor<>(creator);

        // Assuming the election data is contained in {@code src/main/resource} it should be found.
        // Please note that you can also specify an absolute path to the folder!
        Election election = electionProcessor.processResults("TK2023", PathUtils.getResourcePath("/EML_bestanden_TK2023_HvA_UvA"));

        System.out.println("All files are processed.\n");
        // Just print the 'results'
        System.out.println(election);
    }
}
