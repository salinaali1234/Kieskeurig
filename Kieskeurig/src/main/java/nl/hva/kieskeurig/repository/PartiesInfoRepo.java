package nl.hva.kieskeurig.repository;

import nl.hva.ict.se.sm3.demo.DutchElectionTransformerForParties;
import nl.hva.ict.se.sm3.utils.PathUtils;
import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
import nl.hva.kieskeurig.model.CandidateForPartyInfo;
import nl.hva.kieskeurig.model.ElectionForParty;
import nl.hva.kieskeurig.model.PartyWithInfo;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;

@Repository
public class PartiesInfoRepo {

    public ElectionForParty loadElectionData() throws IOException, XMLStreamException { // ✅ XMLStreamException toegevoegd
        System.out.println("loadElectionData");
        DutchElectionTransformerForParties transformer = new DutchElectionTransformerForParties();
        DutchElectionProcessor<ElectionForParty> electionProcessor = new DutchElectionProcessor<>(transformer);
        System.out.println("PartiesInfoRepo.loadElectionData()");
        return electionProcessor.processResults("TK2023", PathUtils.getResourcePath("/EML_bestanden_TK2023_HvA_UvA"));
    }

    public List<PartyWithInfo> getParties() throws IOException, XMLStreamException { // ✅ XMLStreamException toegevoegd
        ElectionForParty election = loadElectionData();
        System.out.println("listwith parties" + election.getParties());
        return election.getParties();
    }

    public PartyWithInfo getPartyById(int partyId) throws IOException, XMLStreamException { // ✅ XMLStreamException toegevoegd
        for (PartyWithInfo party : getParties()) {
            if (party.getPartyId() == partyId) {
                return party;
            }
        }
        return null;
    }

    public List<CandidateForPartyInfo> getCandidatesOfParty(int partyId) throws IOException, XMLStreamException {
        System.out.println("Fetching candidates for party ID: " + partyId);
        PartyWithInfo party = getPartyById(partyId);

                if (party != null) {
            System.out.println("Found " + party.getCandidates().size() + " candidates");
            System.out.println(party.getCandidates());
            return party.getCandidates();
        }

        System.out.println("Party not found");
        return new ArrayList<>();
    }

    public List<ElectionForParty> getAll() throws IOException, XMLStreamException { // ✅ XMLStreamException toegevoegd
        return List.of(loadElectionData());
    }
}

//    public boolean readResults(String folderName) {
//        DutchElectionReader reader = new DutchElectionReader(this); // Correcte manier om een instantie van DutchElectionReader aan te maken
//        return reader.readResults(folderName); // Alleen folderName doorgeven, zonder 'party'
//    }

