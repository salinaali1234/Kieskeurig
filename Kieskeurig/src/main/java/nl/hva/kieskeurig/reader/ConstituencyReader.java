package nl.hva.kieskeurig.reader;

import nl.hva.kieskeurig.utils.xml.DutchElectionProcessor;

import nl.hva.kieskeurig.utils.xml.Transformer;
import nl.hva.kieskeurig.utils.xml.XMLParser;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.*;

/**
 * A very small demo of how the classes {@link DutchElectionProcessor} and {@link Transformer}
 * can be used to process the XML-files.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class ConstituencyReader {
    private XMLParser xmlParser;

    public ConstituencyReader(XMLParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    public Map<String, String> getConstituencyMap() throws IOException, XMLStreamException {
        Map<String, String> constituencyMap = new HashMap<>();

        while (xmlParser.tryNext()) {
            // Find <kr:Region> elements
            if (xmlParser.isStartElement() && DutchElectionProcessor.REGION.equals(xmlParser.getLocalName())) {
                // Get RegionNumber attribute
                int regionNumber = xmlParser.getIntegerAttributeValue(null, DutchElectionProcessor.REGION_NUMBER, 0);
                String regionName = null;
                String regionCatogory = xmlParser.getAttributeValue(null, DutchElectionProcessor.REGION_CATEGORY);

                // Inside <kr:Region> loop
                while (xmlParser.tryNext()) {
                    if (xmlParser.isStartElement() && DutchElectionProcessor.REGION_NAME.equals(xmlParser.getLocalName())) {
                        // Move to text content inside <kr:RegionName>
                        if (xmlParser.tryNext() && xmlParser.isCharacters()) {
                            regionName = xmlParser.getText().trim();
                        }
                    }


                    // End of this <kr:Region> element
                    if (xmlParser.isEndElement() && DutchElectionProcessor.REGION.equals(xmlParser.getLocalName())) {
                        break;
                    }
                }

                // Add to map if both values are present
                if (regionName != null && !regionName.isEmpty() && Objects.equals(regionCatogory, "KIESKRING") ) {
                    constituencyMap.put(String.valueOf(regionNumber), regionName);

                }
            }
        }
        return constituencyMap;
    }
}

