package nl.hva.kieskeurig.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class Party{

    private final int partyId;
    private final String partyName;
    private List<Candidate> candidates = new ArrayList<>();

    public Party (int partyId, String partyName){
        this.partyId = partyId;
        this.partyName = partyName;
    }

    public boolean addCandidate(Candidate candidate){
        for(Candidate c : candidates){
            if(c.getCandidateIdentifier() == candidate.getCandidateIdentifier()){
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