package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.model.PartyWithInfo;
import nl.hva.kieskeurig.repository.PartyInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PartyService {
    private final PartyInfoRepo repo;

    @Autowired
    public PartyService(PartyInfoRepo repo) {
        this.repo = repo;
    }

    public List<PartyWithInfo> getAllParties(String sortOrder) throws XMLStreamException, IOException {
        List<PartyWithInfo> parties = repo.getParties();
        return sortParties(parties, sortOrder);
    }

    public Optional<PartyWithInfo> getParty(int partyId) throws IOException, XMLStreamException {
        return Optional.ofNullable(repo.getPartyById(partyId));
    }

    private List<PartyWithInfo> sortParties(List<PartyWithInfo> parties, String sortOrder) {
        if (parties == null) return List.of();

        return switch (sortOrder) {
            case "seats-asc" -> parties.stream()
                    .sorted(Comparator.comparingInt(PartyWithInfo::getSeats))
                    .toList();
            case "alphabetical" -> parties.stream()
                    .sorted(Comparator.comparing(PartyWithInfo::getPartyName))
                    .toList();
            case "alphabetical-reverse" -> parties.stream()
                    .sorted(Comparator.comparing(PartyWithInfo::getPartyName).reversed())
                    .toList();
            default -> parties.stream()
                    .sorted(Comparator.comparingInt(PartyWithInfo::getSeats).reversed())
                    .toList();
        };
    }
}