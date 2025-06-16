package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.model.ElectionForParty;
import nl.hva.kieskeurig.repository.PartyInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

@Service
public class ElectionService {
    private final PartyInfoRepo repo;

    @Autowired
    public ElectionService(PartyInfoRepo repo) {
        this.repo = repo;
    }

    public List<ElectionForParty> getAll() throws IOException, XMLStreamException {
        return repo.getAll();
    }
}