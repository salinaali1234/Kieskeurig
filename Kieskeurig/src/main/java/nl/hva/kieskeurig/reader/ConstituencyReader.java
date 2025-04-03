package nl.hva.kieskeurig.reader;

import nl.hva.ict.se.sm3.demo.DutchElectionTransformer;
import nl.hva.ict.se.sm3.demo.Election;
import nl.hva.ict.se.sm3.utils.PathUtils;
import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
import nl.hva.ict.se.sm3.utils.xml.XMLParser;
import nl.hva.kieskeurig.service.ConstituencyService;

import javax.sql.rowset.spi.XmlReader;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A very small demo of how the classes {@link DutchElectionProcessor} and {@link nl.hva.ict.se.sm3.utils.xml.Transformer}
 * can be used to process the XML-files.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class ConstituencyReader {
    private XMLParser xmlParser;

    public ConstituencyReader(XMLParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    public Map<Integer, String> getConstituencyMap() throws IOException, XMLStreamException {
        Map<Integer, String> constituencyMap = new HashMap<>();

        while (xmlParser.tryNext()) {
            System.out.println("Reading XML, current element 4: " + xmlParser.getLocalName());  // Debug output

            System.out.println(xmlParser.getLocalName());

//            //System.out.println(PathUtils.getResourcePath("Telling_%s_gemeente"));
//            if (xmlParser.isStartElement() && DutchElectionProcessor.SELECTION.equals(xmlParser.getLocalName())) {
//                System.out.println("Found REPORTING_UNIT_IDENTIFIER!");

        while (xmlParser.tryNext()) {
            System.out.println("Reading REPORTING_UNIT_IDENTIFIER!");
            if (xmlParser.isStartElement() && DutchElectionProcessor.SELECTION.equals(xmlParser.getLocalName())) {
                Integer id = null;
                String value = null;
                System.out.println("this is the reader Constituency" + 1);

                while (xmlParser.tryNext()) {
                    System.out.println("Reading XML, current element reader: " + xmlParser.getLocalName());
                    if (xmlParser.isStartElement()) {
                        String unitIdentifier = xmlParser.getLocalName();
                        System.out.println("step 2");

                        if (DutchElectionProcessor.REPORTING_UNIT_IDENTIFIER.equals(unitIdentifier)) {
                            xmlParser.tryNext();
                            String textValue = xmlParser.getText().trim();

                            if (!textValue.isEmpty()) {
                                try {
                                    id = Integer.parseInt(textValue);
                                } catch (NumberFormatException e) {
                                    System.err.println("Invalid ID format: " + textValue);
                                    continue;
                                }
                            }
                        } else {
                            value = xmlParser.getText().trim();
                        }
                    }

                    if (xmlParser.isEndElement() && DutchElectionProcessor.REPORTING_UNIT_IDENTIFIER.equals(xmlParser.getLocalName())) {
                        break;
                    }
                }

                if (id != null && value != null && !value.isEmpty()) {
                    System.out.println("Reading Constituency: " + id);
                    constituencyMap.put(id, value);
                } else {
                    System.out.println("id: "+ id);
                }
                }
        }
        System.out.println("id 2 " + constituencyMap.size());
        }
            System.out.println("id 3 " + constituencyMap.size());
        return constituencyMap;
    }}

