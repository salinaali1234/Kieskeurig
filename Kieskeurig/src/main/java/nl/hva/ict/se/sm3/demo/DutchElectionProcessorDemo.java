package nl.hva.ict.se.sm3.demo;

import nl.hva.ict.se.sm3.utils.PathUtils;
import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * A very small demo of how the classes {@link DutchElectionProcessor} and {@link nl.hva.ict.se.sm3.utils.xml.Transformer}
 * can be used to process the XML-files.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class DutchElectionProcessorDemo {

    public static void main(String[] args) throws IOException, XMLStreamException {
        System.out.println("Processing files...");

        // We need a Transformer that has knowledge of your classes.
        DutchElectionTransformer creator = new DutchElectionTransformer();

        // And the election processor that traverses the folders and processes the XML-files.
        DutchElectionProcessor<Election> electionProcessor = new DutchElectionProcessor<>(creator);

        // Assuming the election data is contained in {@code src/main/resource} it should be found.
        // Please note that you can also specify an absolute path to the folder!
        Election election= electionProcessor.processResults("TK2023", PathUtils.getResourcePath("/EML_bestanden_TK2023_HvA_UvA"));

        System.out.println("All files are processed.\n");
        // Just print the 'results'
        System.out.println(election.data);
    }

}
