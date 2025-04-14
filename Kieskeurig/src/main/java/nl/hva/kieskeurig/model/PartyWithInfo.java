package nl.hva.kieskeurig.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class PartyWithInfo {

    private final int partyId;
    private final String partyName;
    private List<Candidate> candidates = new ArrayList<>();

    public PartyWithInfo(int partyId, String partyName){
        this.partyId = partyId;
        this.partyName = partyName;
    }

    public boolean addCandidate(Candidate candidate){
        for(Candidate c : candidates){
            if(c.getId() == candidate.getId()){
                return false;
            }
        }
        return candidates.add(candidate);
    }

    @Override
    public String toString() {
        return partyName + "(" + partyId + ")";
    }



}