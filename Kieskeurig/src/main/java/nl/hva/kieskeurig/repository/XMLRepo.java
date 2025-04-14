package nl.hva.kieskeurig.repository;

import nl.hva.ict.se.sm3.demo.DutchElectionTransformer;
import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.Election;
import nl.hva.kieskeurig.model.Party;
import nl.hva.ict.se.sm3.utils.PathUtils;
import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;

@Repository
public class XMLRepo {

    @Autowired
    private DutchElectionProcessor<Election> electionProcessor;


    public Election loadElectionData() throws IOException, XMLStreamException { // ✅ XMLStreamException toegevoegd
        DutchElectionTransformer transformer = new DutchElectionTransformer();

        return electionProcessor.processResults("TK2023", PathUtils.getResourcePath("/EML_bestanden_TK2023_HvA_UvA"));
    }

    public List<Party> getParties() throws IOException, XMLStreamException { // ✅ XMLStreamException toegevoegd
        Election election = loadElectionData();
        return election.getParties();
    }

    public Party getPartyById(int partyId) throws IOException, XMLStreamException { // ✅ XMLStreamException toegevoegd
        for (Party party : getParties()) {
            if (party.getPartyId() == partyId) {
                return party;
            }
        }
        return null;
    }

    public List<Candidate> getCandidatesOfParty(int partyId) throws IOException, XMLStreamException {
        System.out.println("Fetching candidates for party ID: " + partyId);
        Party party = getPartyById(partyId);

        if (party != null) {
            System.out.println("Found " + party.getCandidates().size() + " candidates");
            return party.getCandidates();
        }

        System.out.println("Party not found");
        return new ArrayList<>();
    }

    public List<Election> getAll() throws IOException, XMLStreamException { // ✅ XMLStreamException toegevoegd
        return List.of(loadElectionData());
    }
}

//    public boolean readResults(String folderName) {
//        DutchElectionReader reader = new DutchElectionReader(this); // Correcte manier om een instantie van DutchElectionReader aan te maken
//        return reader.readResults(folderName); // Alleen folderName doorgeven, zonder 'party'
//    }

