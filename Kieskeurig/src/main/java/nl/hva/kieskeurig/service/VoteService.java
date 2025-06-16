package nl.hva.kieskeurig.service;

//import nl.hva.ict.se.sm3.utils.xml.XMLParser;
import nl.hva.kieskeurig.exception.VoteLoadingException;
import nl.hva.kieskeurig.model.Vote;
import nl.hva.kieskeurig.reader.VoteReader;
import nl.hva.kieskeurig.repository.NationalVotesRepo;
import nl.hva.kieskeurig.utils.xml.XMLParser;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.InputStream;
import java.util.*;

@Service
public class VoteService {
    private final NationalVotesRepo voteRepo;
    private final Map<String, List<Vote>> votesPerYear = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(VoteService.class);


    public VoteService(NationalVotesRepo voteRepo) {
        this.voteRepo = voteRepo;
    }

    /**
     * Loads election results for a given year and returns a map of valid votes per party.
     *
     * @param year Type {@link String}
     * @return {@link Map} where keys are party names and values are vote counts
     */
    public Map<String, Integer> getResults(String year) {
//        votesPerYear.remove(year);
        String folder = "Verkiezingsuitslag_Tweede_Kamer_" + year;
        String fileName = getFileNameForYear(year);

        boolean success = readResults(folder, fileName, year);
        if (!success) {
            throw new VoteLoadingException("Could not load results for year " + year);
        }

        return getVotesPerParty(year);
    }

    /**
     * Returns the corresponding file name for a given election year.
     *
     * @param year Type {@link String}
     * @return {@link String} containing the XML file name
     */
    public String getFileNameForYear(String year) {
        return switch (year) {
            case "2021" -> "Totaaltelling_TK2021.eml.xml";
            case "2023" -> "Totaaltelling_TK2023.eml.xml";
            default -> throw new IllegalArgumentException("Invalid year specified: " + year);
        };
    }

    /**
     * Adds a {@link Vote} object to the internal storage for a specific year.
     *
     * @param year Type {@link String}
     * @param vote Type {@link Vote}
     */
    public void add(String year, Vote vote) {
        votesPerYear.computeIfAbsent(year, key -> new ArrayList<>()).add(vote);
    }

    /**
     * Reads vote results from an XML file and stores them in memory and in the database.
     *
     * @param folder Type {@link String}
     * @param fileName Type {@link String}
     * @param year Type {@link String}
     * @return {@link Boolean}
     */
    public boolean readResults(String folder, String fileName, String year) {
        if (!voteRepo.findAllByYear(year).isEmpty()) {
            votesPerYear.put(year, voteRepo.findAllByYear(year));
            return true;
        }

        try {
            logger.info("Trying to load: {}/{}", folder, fileName);

            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream(folder + "/" + fileName);

            if (inputStream == null) {
                return false;
            }

            XMLParser xmlParser = new XMLParser(inputStream);
            VoteReader reader = new VoteReader(xmlParser);

            Map<String, Integer> partyVotes = reader.getValidVotes();

            List<Vote> newVotes = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : partyVotes.entrySet()) {
                String partyName = entry.getKey();
                int votes = entry.getValue();

                Vote vote = new Vote(partyName, votes, year);
                voteRepo.save(vote);
                newVotes.add(vote);
            }

            votesPerYear.put(year, newVotes);
            return true;

        } catch (Exception e) {
            logger.error("Error reading XML files", e);
            return false;
        }
    }

    /**
     * Returns a map of total valid votes per party for a given year.
     *
     * @param year Type {@link String}
     * @return {@link Map} with party names as keys and vote counts as values
     */
    public Map<String, Integer> getVotesPerParty(String year) {
        Map<String, Integer> partyVotes = new HashMap<>();
        List<Vote> votes = votesPerYear.getOrDefault(year, List.of());


        for (Vote vote : votes) {
            partyVotes.put(
                    vote.getPartyName(),
                    partyVotes.getOrDefault(vote.getPartyName(), 0) + vote.getValidVotes()
            );
        }

//        votes.clear();
        return partyVotes;
    }

    /**
     * Returns the election results for a given year, sorted according to the specified sort option.
     *
     * @param year Type {@link String}
     * @param sort Type {@link String}
     * @return {@link Map} where keys are party names and values are vote counts, sorted as specified
     */
    public Map<String, Integer> getSortedResults(String year, String sort) {
        Map<String, Integer> results = getResults(year);

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(results.entrySet());

        switch (sort.toLowerCase()) {
            case "votes-desc":
                sorted.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
                break;
            case "votes-asc":
                sorted.sort(Map.Entry.comparingByValue());
                break;
            case "name-asc":
                sorted.sort(Map.Entry.comparingByKey());
                break;
            case "name-desc":
                sorted.sort(Map.Entry.<String, Integer>comparingByKey().reversed());
                break;
            default:

        }

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : sorted) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}