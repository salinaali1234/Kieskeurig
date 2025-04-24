package nl.hva.kieskeurig.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ElectionForParty {
    private String electionDate;

    private List<PartyWithInfo> parties = new ArrayList<>();

    public ElectionForParty(String electionDate) {this.electionDate = electionDate;}
    public PartyWithInfo getParty(int partyId) {
        for (PartyWithInfo party : parties) {
            if (party.getPartyId() == partyId) {
                return party;
            }
        }
        return null;
    }

    public List<CandidateForPartyInfo> getAllCandidates() {
        return parties.stream()
                .flatMap(party -> party.getCandidates().stream())
                .collect(Collectors.toList());
    }
    public boolean addParty(PartyWithInfo party) {
        return parties.add(party);
    }

    @Override
    public String toString() {
        return "Election: %s - #of parties: %d".formatted(electionDate, parties.size());
    }
}