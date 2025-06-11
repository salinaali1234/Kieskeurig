//package nl.hva.kieskeurig.service;
//
//import nl.hva.kieskeurig.model.CandidateForPartyInfo;
//import nl.hva.kieskeurig.model.ElectionForParty;
//import nl.hva.kieskeurig.model.PartyWithInfo;
//import nl.hva.kieskeurig.repository.PartyInfoRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Comparator;
//import java.util.Optional;
//import java.io.IOException;
//import java.util.List;
//import javax.xml.stream.XMLStreamException;
//
//@Service
//public class PartyInfoService {
//    private final PartyInfoRepo repo;
//
//    @Autowired
//    public PartyInfoService(PartyInfoRepo repo) {
//        this.repo = repo;
//    }
//
//    // gets election data
//    public List<ElectionForParty> getAll() throws IOException, XMLStreamException {
//        return repo.getAll();
//    }
//
//    // picks up party
//    public List<PartyWithInfo> getAllParties(String sortOrder) throws IOException, XMLStreamException {
//        List<PartyWithInfo> parties = repo.getParties();
//        return sortParties(parties, sortOrder);
//    }
//
//    public Optional<PartyWithInfo> getParty(int partyId) throws IOException, XMLStreamException {
//        return Optional.ofNullable(repo.getPartyById(partyId));
//    }
//
//    // note to self: picks up candidate
//    public List<CandidateForPartyInfo> getCandidatesOfParty(int partyId) throws IOException, XMLStreamException {
//        return repo.getCandidatesOfParty(partyId);
//    }
//
//    public List<CandidateForPartyInfo> getAllCandidates() throws IOException, XMLStreamException {
//        return repo.getAllCandidates();
//    }
//
//
//    private List<PartyWithInfo> sortParties(List<PartyWithInfo> parties, String sortOrder) {
//        if (parties == null) {
//            return List.of();
//        }
//
//        return switch (sortOrder) {
//            case "seats-asc" -> parties.stream()
//                    .sorted(Comparator.comparingInt(PartyWithInfo::getSeats))
//                    .toList();
//            case "alphabetical" -> parties.stream()
//                    .sorted(Comparator.comparing(PartyWithInfo::getPartyName))
//                    .toList();
//            case "alphabetical-reverse" -> parties.stream()
//                    .sorted(Comparator.comparing(PartyWithInfo::getPartyName).reversed())
//                    .toList();
//            default -> parties.stream()
//                    .sorted(Comparator.comparingInt(PartyWithInfo::getSeats).reversed())
//                    .toList();
//        };
//    }
//}
////    public boolean readResults(String folderName) {
////        return repo.readResults(folderName);
////    }
