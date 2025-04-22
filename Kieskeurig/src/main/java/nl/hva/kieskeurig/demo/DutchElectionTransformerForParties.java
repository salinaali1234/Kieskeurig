package nl.hva.kieskeurig.demo;


//import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
import nl.hva.kieskeurig.utils.xml.Transformer;
//import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.CandidateForPartyInfo;
import nl.hva.kieskeurig.model.ElectionForParty;
import nl.hva.kieskeurig.model.PartyWithInfo;


import java.util.Map;

import static nl.hva.kieskeurig.utils.xml.DutchElectionProcessor.*;

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
        if (election == null) {
            election = new ElectionForParty(electionDate);
//            LOG.info("Election initialized with date: " + electionDate);
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
        PartyWithInfo party = new PartyWithInfo(partyId, partyName);
        election.addParty(party);
//        LOG.info("Registered party: " + partyName);
    }


    @Override
    public void registerCandidate(Map<String, String> candidateData) {
//        System.out.printf("Found candidate information: %s\n", candidateData);
        int partyId = Integer.parseInt(candidateData.get(AFFILIATION_IDENTIFIER));
        PartyWithInfo party = election.getParty(partyId);

        if (party != null) {
            int candidateId = Integer.parseInt(candidateData.get(CANDIDATE_IDENTIFIER));
            String firstName = candidateData.get(FIRST_NAME);
            String lastName = candidateData.get(LAST_NAME);
            String gender = candidateData.get(GENDER);
            String localityName = candidateData.getOrDefault("locality", "Unknown");

            // Haal de elected-status op uit candidateData (standaard "no" als niet aanwezig)
            String elected = candidateData.getOrDefault(ELECTED, "no");
            String test = candidateData.get(ELECTED);
//            System.out.println("====");
//            System.out.println(test);
            boolean isElected = "yes".equalsIgnoreCase(elected);

            CandidateForPartyInfo candidate = new CandidateForPartyInfo(
                    candidateId, partyId, firstName, lastName, gender, localityName, isElected
            );

            party.addCandidate(candidate);
        }
    }

    @Override
    public void registerVotes(Map<String, String> votesData) {
        System.out.println("📥 registerVotes: " + votesData); // 👈 logging!

        int partyId = Integer.parseInt(votesData.get(AFFILIATION_IDENTIFIER));
        int candidateId = Integer.parseInt(votesData.get(CANDIDATE_IDENTIFIER));
        PartyWithInfo party = election.getParty(partyId);

        if (party != null) {
            CandidateForPartyInfo candidate = new CandidateForPartyInfo(
                    candidateId,
                    partyId,
                    "", "", "", "Onbekend", false
            );
            party.addCandidate(candidate);
            party.incrementSeats();
        }
    }
    @Override
    public void registerConstituents(Map<String, String> constituentData) {

    }

    public void registerElected(Map<String, String> electedData) {
        // haal de id op (zowel party als canddiates)
        // zoek de candidate m.b.v. election.candidate.find(x =>
        // candidate.setElected = electedData.get(ELECTED);
    }

    @Override
    public ElectionForParty retrieve() {
        return election;
    }
}
