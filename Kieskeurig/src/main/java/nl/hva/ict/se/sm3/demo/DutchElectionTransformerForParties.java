package nl.hva.ict.se.sm3.demo;


//import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
import nl.hva.ict.se.sm3.utils.xml.Transformer;
//import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.CandidateForPartyInfo;
import nl.hva.kieskeurig.model.ElectionForParty;
import nl.hva.kieskeurig.model.PartyWithInfo;


import java.util.Map;

import static nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor.*;

/**
 * A dummy {@link Transformer} that just prints the election data so you can get an understanding of what
 * information is available within each method.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class DutchElectionTransformerForParties implements Transformer<ElectionForParty> {
  //  private Election election = new Election();
    private ElectionForParty election;

    @Override
    public void registerElection(Map<String, String> electionData) {
        String electionDate = electionData.get(ELECTION_DATE);
        //System.out.printf("Found election information: %s\n", electionData);
        if (election == null) {
            election = new ElectionForParty(electionDate);
        }
    }

    @Override
    public void registerContest(Map<String, String> contestData) {
      // election = new Election(electionDate);

        //System.out.printf("Found contest information: %s\n", contestData);
    }


    @Override
    public void registerAffiliation(Map<String, String> affiliationData) {
        int partyId = Integer.parseInt(affiliationData.get(AFFILIATION_IDENTIFIER));
        String partyName = affiliationData.get(REGISTERED_NAME);

        //System.out.println("Registering party: ID=" + partyId + ", Name=" + partyName);

        PartyWithInfo party = new PartyWithInfo(partyId, partyName);
        election.addParty(party);
        //System.out.println(election);
    }


    @Override
    public void registerCandidate(Map<String, String> candidateData) {
        //System.out.println("Registering candidate: " + candidateData);

        int partyId = Integer.parseInt(candidateData.get(AFFILIATION_IDENTIFIER));
        PartyWithInfo party = election.getParty(partyId);

        if (party != null) {

            int candidateId = Integer.parseInt(candidateData.get(CANDIDATE_IDENTIFIER));
            String firstName = candidateData.get(FIRST_NAME);
            String lastName = candidateData.get(LAST_NAME);
            String gender = candidateData.get(GENDER);
            String localityName = candidateData.getOrDefault("locality", null);

            CandidateForPartyInfo candidate = new CandidateForPartyInfo(candidateId, partyId, firstName, lastName, gender, "Unknown");
            party.addCandidate(candidate);


            System.out.println("Added candidate " + candidateId + firstName + " to party " + partyId);
        } else {
            System.out.println("Party " + partyId + " not found for candidate");
        }
    }

    @Override
    public void registerVotes(Map<String, String> votesData) {
       // election.data = votesData;
       // System.out.printf("Found votes information: %s\n", votesData);
    }

    @Override
    public void registerConstituents(Map<String, String> constituentData) {

    }

    @Override
    public ElectionForParty retrieve() {
        return election;
    }
}
