package nl.hva.kieskeurig.reader;

import nl.hva.kieskeurig.utils.xml.DutchElectionProcessor;
import nl.hva.kieskeurig.utils.xml.XMLParser;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.*;

public class RegionReader {
    XMLParser xmlParser;

    public RegionReader(XMLParser parser) {this.xmlParser = parser;}

    public Map<Integer, Map<String, Integer>> getAllRegions() throws IOException, XMLStreamException {
        Map<Integer, Map<String, Integer>> regionsMap = new HashMap<>();

        while (xmlParser.tryNext()) {
            // Find <kr:Region> elements
            if (xmlParser.isStartElement() && DutchElectionProcessor.REGION.equals(xmlParser.getLocalName())) {
                // Get RegionNumber attribute
                int regionNumber = xmlParser.getIntegerAttributeValue(null, DutchElectionProcessor.REGION_NUMBER, 0);
                String regionCatogory = xmlParser.getAttributeValue(null, DutchElectionProcessor.REGION_CATEGORY);
                int superiorRegionNumber = xmlParser.getIntegerAttributeValue(null, DutchElectionProcessor.SUPERIOR_REGION_NUMBER, 0);
                String regionName = null;
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


                Map<String, Integer> regionsInfoMap = new HashMap<>();
                if (regionName != null && !regionName.isEmpty()) {
                    if (Objects.equals(regionCatogory, "KIESKRING")) {
                        superiorRegionNumber = 0;
                    }
                    regionsInfoMap.put(regionName, superiorRegionNumber);
                    regionsMap.put(regionNumber,regionsInfoMap);
                }


            }
        }
        return regionsMap;
    }

}
