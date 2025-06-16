package nl.hva.kieskeurig.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
public class PartyWithInfo {
    private final int partyId;
    private final String partyName;
    private List<CandidateForPartyInfo> candidates = new ArrayList<>();
    private int seats = 0;

    public PartyWithInfo(int partyId, String partyName) {
        this.partyId = partyId;
        this.partyName = partyName;
    }

    public boolean addCandidate(CandidateForPartyInfo candidate) {
        for (CandidateForPartyInfo c : candidates) {
            if (c.getId() == candidate.getId()) {
                return false;
            }
        }
        return candidates.add(candidate);
    }

    public void incrementSeats() {
        this.seats++;
    }

    @Override
    public String toString() {
        return partyName + "(" + partyId + ")";
    }
}