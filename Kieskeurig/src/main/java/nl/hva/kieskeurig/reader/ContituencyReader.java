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
import java.util.HashMap;
import java.util.Map;

/**
 * A very small demo of how the classes {@link DutchElectionProcessor} and {@link nl.hva.ict.se.sm3.utils.xml.Transformer}
 * can be used to process the XML-files.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class ContituencyReader {
    private XMLParser xmlParser;

    public ContituencyReader(XMLParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    public  Map<Integer, String> getContituencyMap() throws IOException, XMLStreamException {
        Map<Integer, String> contituencyMap = new HashMap<Integer, String>();

        while (xmlParser.tryNext()) {
            if (xmlParser.isStartElement() && DutchElectionProcessor.REPORTING_UNIT_IDENTIFIER.equals(xmlParser.getLocalName())) {
                int id = 0;

                while (xmlParser.tryNext()) {
                    String unitIdentifier = xmlParser.getLocalName();
                    if (DutchElectionProcessor.REPORTING_UNIT_IDENTIFIER.equals(unitIdentifier)) {
                        xmlParser.tryNext();
                        String textValue = xmlParser.getText().trim();
                        if (!textValue.isEmpty()) {
                            id = Integer.parseInt(textValue);
                        }
                    }

                }
                if (id != 0) {
                    contituencyMap.put(Integer.valueOf((id + "hi")), xmlParser.getText());
                }

            }
        }
        return contituencyMap;
    }
}