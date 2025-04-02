package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.Election;
import nl.hva.kieskeurig.model.Party;
import nl.hva.kieskeurig.repository.XMLRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.XMLStreamException;

@Service
public class XMLService {
    private final XMLRepo repo;

    @Autowired
    public XMLService(XMLRepo repo) {
        this.repo = repo;
    }

    // gets election data
    public List<Election> getAll() throws IOException, XMLStreamException {
        return repo.getAll();
    }

    // picks up party
    public List<Party> getAllParties() throws IOException, XMLStreamException {
        return repo.getParties();
    }

    public Optional<Party> getParty(int partyId) throws IOException, XMLStreamException {
        return Optional.ofNullable(repo.getPartyById(partyId));
    }

    // note to self: picks up candidate (does not work yet)
    public List<Candidate> getCandidatesOfParty(int partyId) throws IOException, XMLStreamException {
        return repo.getCandidatesOfParty(partyId);
    }
}
//    public boolean readResults(String folderName) {
//        return repo.readResults(folderName);
//    }
