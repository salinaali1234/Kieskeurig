package nl.hva.kieskeurig.reader;

import nl.hva.ict.se.sm3.utils.xml.XMLParser;

import javax.xml.stream.XMLStreamException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DutchElectionReader {
    private XMLParser xmlParser;

    public DutchElectionReader(XMLParser xmlParser) {
        this.xmlParser = xmlParser;

    }

    public Map<String, Integer> getValidVotes() throws XMLStreamException {
        Map<String, Integer> partyVotes = new HashMap<>();

        while (xmlParser.findBeginTag("Selection")) {
            String partyName = null;
            int votes = 0;

            while (xmlParser.tryNext()) {
                String tagName = xmlParser.getLocalName();

                if ("RegisteredName".equals(tagName) && xmlParser.isStartElement()) {
                    xmlParser.tryNext();
                    partyName = xmlParser.getText().trim();
                }
                else if ("ValidVotes".equals(tagName) && xmlParser.isStartElement()) {
                    xmlParser.tryNext();
                    votes = Integer.parseInt(xmlParser.getText().trim());
                }
                else if ("Selection".equals(tagName) && xmlParser.isEndElement()) {
                    break;
                }
            }

            if (partyName != null) {
                partyVotes.put(partyName, partyVotes.getOrDefault(partyName, 0) + votes);
            }
        }
        return partyVotes;
    }
}
