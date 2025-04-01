package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.Election;
import nl.hva.kieskeurig.model.Party;
import nl.hva.kieskeurig.repository.XMLRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class XMLService {
    private final XMLRepo repo;

    @Autowired
    public XMLService(XMLRepo repo) {
        this.repo = repo;
    }

//    public List<Party> getAllParties() {
//        return repo.getAllParties();
//    }
//
//    public Optional<Party> getParty(int partyId) {
//        return repo.getParty(partyId);
//    }

    public List<Election> getAll() {
        return repo.getAll();
    }

    public List<Candidate> getCandidatesOfParty(int partyId) {
        return repo.getCandidatesOfParty(partyId);
    }

    public List<Party> getParty(int partyId) {
        return repo.getParty(partyId);
    }

//    public boolean readResults(String folderName) {
//        return repo.readResults(folderName);
//    }
}