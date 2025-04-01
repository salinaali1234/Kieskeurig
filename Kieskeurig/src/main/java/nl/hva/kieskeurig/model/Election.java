package nl.hva.kieskeurig.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Election {
    private String electionDate;

    private List<Party> parties = new ArrayList<>();

    public Election(String electionDate) {this.electionDate = electionDate;}
    public Party getParty(int partyId) {
        for (Party party : parties) {
            if (party.getPartyId() == partyId) {
                return party;
            }
        }
        return null;
    }

    public boolean addParty(Party party) {
        return parties.add(party);
    }

    @Override
    public String toString() {
        return "Election: %s - #of parties: %d".formatted(electionDate, parties.size());
    }
}