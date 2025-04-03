package nl.hva.kieskeurig.reader;

import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
import nl.hva.ict.se.sm3.utils.xml.XMLParser;

import javax.xml.stream.XMLStreamException;
import java.util.HashMap;
import java.util.Map;


public class NationalVotesReader {
    private XMLParser xmlParser;

    public NationalVotesReader(XMLParser xmlParser) {
        this.xmlParser = xmlParser;

    }

    public Map<String, Integer> getValidVotes() throws XMLStreamException {
        Map<String, Integer> partyVotes = new HashMap<>();

        while (xmlParser.tryNext()) {

            if (xmlParser.isStartElement() && DutchElectionProcessor.TOTAL_COUNTED.equals(xmlParser.getLocalName())) {
                break;
            }

            if (xmlParser.isStartElement() && DutchElectionProcessor.SELECTION.equals(xmlParser.getLocalName())) {
                String partyName = null;
                int votes = 0;

                while (xmlParser.tryNext()) {
                    if (xmlParser.isStartElement()) {
                        String tagName = xmlParser.getLocalName();

                        if (DutchElectionProcessor.REGISTERED_NAME.equals(tagName)) {
                            xmlParser.tryNext();
                            partyName = xmlParser.getText().trim();

                        } else if (DutchElectionProcessor.VALID_VOTES.equals(tagName)) {
                            xmlParser.tryNext();
                            String textValue = xmlParser.getText().trim();
                            if (!textValue.isEmpty()) {
                                votes = Integer.parseInt(textValue);
                            }
                        }
                    } else if (xmlParser.isEndElement() && DutchElectionProcessor.SELECTION.equals(xmlParser.getLocalName())) {
                        break;
                    }
                }
                if (partyName != null) {
                    partyVotes.put(partyName, partyVotes.getOrDefault(partyName, 0) + votes);
                }
            }
        }
        return partyVotes;
    }

}
