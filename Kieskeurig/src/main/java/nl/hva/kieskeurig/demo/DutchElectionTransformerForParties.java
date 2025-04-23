package nl.hva.kieskeurig.demo;


//import nl.hva.ict.se.sm3.utils.xml.DutchElectionProcessor;
import nl.hva.kieskeurig.utils.xml.Transformer;
//import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.CandidateForPartyInfo;
import nl.hva.kieskeurig.model.ElectionForParty;
import nl.hva.kieskeurig.model.PartyWithInfo;


import java.util.Map;
import java.util.Optional;

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
        System.out.println("📥 registerVotes: " + votesData);
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

    @Override
    public void registerElected(Map<String, String> electedData) {
        try {
            int partyId = Integer.parseInt(electedData.get(AFFILIATION_IDENTIFIER));
            int candidateId = Integer.parseInt(electedData.get(CANDIDATE_IDENTIFIER));

            PartyWithInfo party = election.getParty(partyId);
            if (party == null) {
                System.out.println("Party not found: " + partyId);
                return;
            }
            party.getCandidates().stream()
                    .filter(c -> c.getCandidateId() == candidateId)
                    .findFirst()
                    .ifPresentOrElse(
                            candidate -> {
                                candidate.setElected(true);
                                System.out.println("✅ Marked elected: " + candidate.getFirstName()
                                        + " " + candidate.getLastName());
                            },
                            () -> System.out.println("❌ Candidate not found: " + candidateId)
                    );
        } catch (Exception e) {
            System.out.println("Error in registerElected: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    public void registerElected(Map<String, String> electedData) {
//        // haal de id op (zowel party als canddiates)
        // zoek de candidate m.b.v. election.candidate.find(x =>
//        // candidate.setElected = electedData.get(ELECTED);
//    }

    @Override
    public ElectionForParty retrieve() {
        return election;
    }
}

//@Override
//public void registerElected(Map<String, String> electedData) {
//    // 1️⃣ Haal de party- en candidate-ID op uit de map
//    int partyId = Integer.parseInt(electedData.get(AFFILIATION_IDENTIFIER));
//    int candidateId = Integer.parseInt(electedData.get(CANDIDATE_IDENTIFIER));
//
//    // 2️⃣ Haal de elected status op
//    String elected = electedData.getOrDefault(ELECTED, "no");
//    boolean isElected = "yes".equalsIgnoreCase(elected);
//
//    // 3️⃣ Zoek de juiste partij op basis van partyId
//    PartyWithInfo party = election.getParty(partyId);
//    if (party == null) {
//        System.out.println("❌ Partij met ID " + partyId + " niet gevonden");
//        return;
//    }
//
//    // 4️⃣ Zoek de kandidaat binnen de partij
//    CandidateForPartyInfo candidate = party.getCandidates().stream()
//            .filter(c -> c.getCandidateId() == candidateId)
//            .findFirst()
//            .orElse(null);
//
//    // 5️⃣ Als kandidaat bestaat, update de elected status
//    if (candidate != null) {
//        candidate.setElected(isElected);
//        System.out.println("✅ Kandidaat " + candidateId + " van partij " + partyId + " gemarkeerd als elected: " + isElected);
//    } else {
//        // 6️⃣ Anders: voeg een nieuwe kandidaat toe (alleen ID + elected status, andere info onbekend)
//        CandidateForPartyInfo newCandidate = new CandidateForPartyInfo(
//                candidateId, partyId, "", "", "", "Onbekend", isElected
//        );
//        party.addCandidate(newCandidate);
//        System.out.println("➕ Nieuwe kandidaat toegevoegd (ID " + candidateId + ") met elected: " + isElected);
//    }
