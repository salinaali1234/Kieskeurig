package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.model.CandidateForPartyInfo;
import nl.hva.kieskeurig.repository.PartyInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;


@Service
public class CandidatePartyService {
    private final PartyInfoRepo repo;

    @Autowired
        public CandidatePartyService(PartyInfoRepo repo) {
            this.repo = repo;
        }

        public List<CandidateForPartyInfo> getCandidatesOfParty(int partyId) throws IOException, XMLStreamException {
            return repo.getCandidatesOfParty(partyId);
        }

        public List<CandidateForPartyInfo> getAllCandidates() throws IOException, XMLStreamException {
            return repo.getAllCandidates();
        }
    }