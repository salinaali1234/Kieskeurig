package nl.hva.kieskeurig.demo;


//import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
//import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.Candidate;
//import nl.hva.kieskeurig.model.Election;
import nl.hva.kieskeurig.model.Party;

import nl.hva.kieskeurig.utils.xml.Transformer;
import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.service.CandidateService;
import nl.hva.kieskeurig.mapper.CandidateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static nl.hva.kieskeurig.utils.xml.DutchElectionProcessor.*;

/**
 * A dummy {@link Transformer} that just prints the election data so you can get an understanding of what
 * information is available within each method.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
@Component
public class DutchElectionTransformer implements Transformer<Election> {
    private final Election election = new Election();

    @Autowired
    private CandidateService candidateService;

    @Override
    public void registerElection(Map<String, String> electionData) {
        String electionDate = electionData.get(ELECTION_DATE);
        System.out.printf("Found election information: %s\n", electionData);
    }

    @Override
    public void registerContest(Map<String, String> contestData) {
      // election = new Election(electionDate);

        System.out.printf("Found contest information: %s\n", contestData);
    }


    @Override
    public void registerAffiliation(Map<String, String> affiliationData) {
        int partyId = Integer.parseInt(affiliationData.get(AFFILIATION_IDENTIFIER));
        String partyName = affiliationData.get(REGISTERED_NAME);

        System.out.println("Registering party: ID=" + partyId + ", Name=" + partyName);

        Party party = new Party(partyId, partyName);
//        election.addParty(party);
        System.out.println(election);
    }


    @Override
    public void registerCandidate(Map<String, String> candidateData) {

//        int partyId = Integer.parseInt(candidateData.get(AFFILIATION_IDENTIFIER));
//        Party party = election.getParty(partyId);

        election.data = candidateData;
        System.out.printf("Found candidate information: %s\n", candidateData);

        Candidate candidate = new CandidateMapper().apply(candidateData);
        candidateService.addCandidate(candidate);
        
//        party.addCandidate(candidate);
    }

    @Override
    public void registerVotes(Map<String, String> votesData) {
       // election.data = votesData;
        System.out.printf("Found votes information: %s\n", votesData);
    }

    @Override
    public Election retrieve() {
        return election;
    }
}
