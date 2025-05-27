package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.enums.Province;
import nl.hva.kieskeurig.exception.InvalidPathVariableException;
import nl.hva.kieskeurig.exception.InvalidRequestParameterException;
import nl.hva.kieskeurig.model.Vote;
import nl.hva.kieskeurig.reader.VoteReader;
import nl.hva.kieskeurig.utils.xml.XMLParser;
import org.springframework.stereotype.Service;
import nl.hva.kieskeurig.controller.ProvinceController;

import java.io.InputStream;
import java.util.*;

/**
 * Handles the logic of {@link ProvinceController}
 */
@Service
public class ProvinceService {
    private final Map<String, List<Vote>> votesPerYear = new HashMap<>();

    public List<String> getProvinces() {
        ArrayList<String> provinces = new ArrayList<>();

        for (Province province : Province.values()) {
            provinces.add(province.getDisplayName());
        }

        return provinces;
    }

    public void readResults(String folder, String fileName, String year) {
        try {
            System.out.println("Trying to load: " + folder + "/" + fileName);

            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream(folder + "/" + fileName);

            if (inputStream == null) {
                return;
            }

            System.out.println(inputStream);

            XMLParser xmlParser = new XMLParser(inputStream);
            VoteReader reader = new VoteReader(xmlParser);

            Map<String, Integer> partyVotes = reader.getValidVotes();

            List<Vote> newVotes = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : partyVotes.entrySet()) {
                String partyName = entry.getKey();
                int votes = entry.getValue();

                Vote vote = new Vote(partyName, votes, year);
//                provinceVotesRepo.save(vote);
                newVotes.add(vote);

            }

            votesPerYear.put(year, newVotes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getVotesPerPartyByElectionByProvince(String electionId, String province, String sort, boolean asc) {
        electionId = electionId.toUpperCase();
        String fileName;
        String year = electionId.replace("TK", "");

        // Error handling
        if (!(electionId.replace("TK", "").replaceAll("\\d", "")).isEmpty()) {
            throw new InvalidPathVariableException("Invalid electionId: " + electionId);
        }
        if (!new YearService().getYears().contains(year)) {
            throw new InvalidPathVariableException("Invalid year: " + year);
        }
        if (!Arrays.stream(Province.values())
                .map(p -> p.getDisplayName().toUpperCase())
                .toList()
                .contains(province.toUpperCase())
        ) {
            throw new InvalidPathVariableException("Invalid province: " + province);
        }

        // Read the results for every constituency for the selected province
        for (Province provinceName : Province.values()) {
            if (provinceName.getDisplayName().equalsIgnoreCase(province)) {
                System.out.println(provinceName);
                for (String constituency : provinceName.getConstituencies()) {
                    System.out.println(constituency);
                    fileName = "Kieskring tellingen/Telling_%s_kieskring_%s.eml.xml".formatted(
                            electionId,
                            constituency.replaceAll("'", "")
                    );

                    readResults("Verkiezingsuitslag_Tweede_Kamer_%s".formatted(year), fileName, electionId);
                }
            }
        }

        List<Vote> totalPartyVotesList = getVotes(electionId);
        return sortVotes(totalPartyVotesList, sort, asc);
    }

    /**
     * Gets the total amount of valid votes each party got and returns it in a {@link List<Vote>} containing objects with the {@link Vote} class.
     * @param electionId Type {@link String}
     * @return {@link List<Vote>}
     */
    private List<Vote> getVotes(String electionId) {
        // Calculate the total votes for each party and put it in a map to get rid of duplicates
        List<Vote> votes = votesPerYear.getOrDefault(electionId, List.of());
        Map<String, Integer> totalPartyVotesMap = new HashMap<>();
        for (Vote vote : votes) {
            totalPartyVotesMap.put(
                    vote.getPartyName(),
                    totalPartyVotesMap.getOrDefault(vote.getPartyName(), 0) + vote.getValidVotes()
            );

        }

        // Turn the map into a list containing the total votes for each party
        List<Vote> totalPartyVotesList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : totalPartyVotesMap.entrySet()) {
            Vote partyVoteResult = new Vote(entry.getKey(), entry.getValue(), electionId);
            totalPartyVotesList.add(partyVoteResult);
        }


        votes.clear();

        return totalPartyVotesList;
    }

    /**
     * Takes a {@link List<Vote>} containing objects with the {@link Vote} class and will return a sorted {@link List<Vote>} containing objects of the same class based on the given parameters.
     * @param votes Type {@link List<Vote>}
     * @param sort Type {@link String}
     * @param asc Type {@link Boolean}
     * @return {@link List<Vote>}
     */
    private List<Vote> sortVotes(List<Vote> votes, String sort, boolean asc) {
        if (sort.equalsIgnoreCase("partyName")) {
            votes.sort(Comparator.comparing(Vote::getPartyName));
        } else if (sort.equalsIgnoreCase("validVotes")) {
            votes.sort(Comparator.comparing(Vote::getValidVotes));
        } else {
            throw new InvalidRequestParameterException("Invalid sort parameter: " + sort);
        }

        if (!asc) Collections.reverse(votes);

        return votes;
    }
}
