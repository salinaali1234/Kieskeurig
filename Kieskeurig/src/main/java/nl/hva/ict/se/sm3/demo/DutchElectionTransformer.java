package nl.hva.ict.se.sm3.demo;

//import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
import nl.hva.ict.se.sm3.utils.xml.Transformer;


import java.util.Map;

/**
 * A dummy {@link Transformer} that just prints the election data so you can get an understanding of what
 * information is available within each method.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
public class DutchElectionTransformer implements Transformer<Election> {
    private Election election = new Election();

    @Override
    public void registerElection(Map<String, String> electionData) {
        election.data = electionData;
        System.out.printf("Found election information: %s\n", electionData);
    }

    @Override
    public void registerContest(Map<String, String> contestData) {
        election.data = contestData;
        System.out.printf("Found contest information: %s\n", contestData);
    }

    @Override
    public void registerAffiliation(Map<String, String> affiliationData) {
      //  String partyName = affiliationData.get(DutchElectionProcessor.REGISTERED_NAME);
      //  int partyId = Integer.parseInt(affiliationData.get(DutchElectionProcessor.AFFILIATION_IDENTIFIER));
        //  System.out.println(partyName + = = + partyId);
       //  Party party = new Party(partyName, partyId);
      //  System.out.println(party);
        election.data = affiliationData;
        System.out.printf("Found affiliation information: %s\n", affiliationData);
    }

    @Override
    public void registerCandidate(Map<String, String> candidateData) {
        election.data = candidateData;
        System.out.println(candidateData);
        System.out.printf("Found candidate information: %s\n", candidateData);
    }

    @Override
    public void registerVotes(Map<String, String> votesData) {
        election.data = votesData;
        System.out.printf("Found votes information: %s\n", votesData);
    }

    @Override
    public Election retrieve() {
        return election;
    }
}
