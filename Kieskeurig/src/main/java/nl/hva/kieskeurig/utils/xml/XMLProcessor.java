package nl.hva.kieskeurig.utils.xml;

import nl.hva.kieskeurig.demo.Election;
import nl.hva.kieskeurig.utils.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * Processes all XML
 */
@Component
public class XMLProcessor implements CommandLineRunner {
    @Autowired
    private DutchElectionProcessor<Election> dutchElectionProcessor;

    @Override
    public void run(String... args) throws Exception {
        xmlProcessing();
    }

    /**
     * This function processes the XML using the code from Nico's DutchElectionProcessorDemo
     * @throws IOException
     * @throws XMLStreamException
     */
    private void xmlProcessing() throws IOException, XMLStreamException {
        System.out.println("Processing files...");

        // Assuming the election data is contained in {@code src/main/resource} it should be found.
        // Please note that you can also specify an absolute path to the folder!
        Election election = dutchElectionProcessor.processResults("TK2023", PathUtils.getResourcePath("/EML_bestanden_TK2023_HvA_UvA"));

        System.out.println("All files are processed.\n");
        // Just print the 'results'
        System.out.println(election);
    }
}
