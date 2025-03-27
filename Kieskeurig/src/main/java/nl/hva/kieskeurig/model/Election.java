package nl.hva.kieskeurig.model;

import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

public class Election {
    @Setter
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
