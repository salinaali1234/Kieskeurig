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

    public Map<String, String> getConstituencyMap() throws IOException, XMLStreamException {
        Map<String, String> constituencyMap = new HashMap<>();

        while (xmlParser.tryNext()) {
            System.out.println(xmlParser.getLocalName());
            System.out.println("this is the reader Constituency");
            while (xmlParser.tryNext()) {
                if (xmlParser.isStartElement() && DutchElectionProcessor.REPORTING_UNIT_VOTES.equals(xmlParser.getLocalName())) {
                    String id = null;
                    String value = null;


                    while (xmlParser.tryNext()) {
                        if (xmlParser.isStartElement()) {
                            String unitIdentifier = xmlParser.getLocalName();
                            if (DutchElectionProcessor.REPORTING_UNIT_IDENTIFIER.equals(unitIdentifier)) {
                                xmlParser.tryNext();
                                String textValue = xmlParser.getText().trim();

                                if (!textValue.isEmpty()) {
                                    try {
                                        id = textValue;
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

                    if (id != null) {
                        System.out.println("Reading Constituency: " + id);
                        constituencyMap.put(id, id);
                    } else {
                        System.out.println("id: " + id);
                    }
                }
            }

        }
        return constituencyMap;
    }}

