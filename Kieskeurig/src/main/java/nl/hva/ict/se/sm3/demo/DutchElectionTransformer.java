package nl.hva.ict.se.sm3.demo;

import nl.hva.ict.se.sm3.utils.xml.Transformer;
import nl.hva.kieskeurig.KieskeurigApplication;
import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.repository.CandidateRepo;
import nl.hva.kieskeurig.service.CandidateService;
import nl.hva.kieskeurig.transformer.CandidateTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * A dummy {@link Transformer} that just prints the election data so you can get an understanding of what
 * information is available within each method.
 * <br>
 * <b>Please do NOT include this code in you project!</b>
 */
@Component
public class DutchElectionTransformer implements Transformer<Election> {
    private Election election = new Election();

    @Autowired
    private CandidateService candidateService;

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
        election.data = affiliationData;
        System.out.printf("Found affiliation information: %s\n", affiliationData);
    }

    @Override
    public void registerCandidate(Map<String, String> candidateData) {
//        ApplicationContext context = new AnnotationConfigApplicationContext(KieskeurigApplication.class);
//        CandidateService candidateService = context.getBean(CandidateService.class);
//        CandidateService candidateService = new CandidateService(new CandidateRepo());

        election.data = candidateData;
        System.out.printf("Found candidate information: %s\n", candidateData);

        Candidate candidate = CandidateTransformer.transformCandidate(candidateData);
        System.out.println(candidate);
        candidateService.addCandidate(candidate);
    }

    @Override
    public void registerVotes(Map<String, String> votesData) {
        election.data = votesData;
//        System.out.printf("Found votes information: %s\n", votesData);
    }

    @Override
    public Election retrieve() {
        return election;
    }
}
