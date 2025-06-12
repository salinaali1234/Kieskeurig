package nl.hva.kieskeurig.service;

import nl.hva.kieskeurig.exception.VoteLoadingException;
import nl.hva.kieskeurig.model.Vote;
import nl.hva.kieskeurig.repository.NationalVotesRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private NationalVotesRepo nationalVotesRepo;

    @InjectMocks
    private VoteService voteService;

    @Test
    void getResults_shouldThrowException_whenReadResultsFails() {
        VoteService spyService = spy(voteService);
        doReturn(false).when(spyService).readResults(anyString(), anyString(), eq("2021"));

        VoteLoadingException ex = assertThrows(VoteLoadingException.class, () -> spyService.getResults("2021"));
        assertEquals("Could not load results for year 2021", ex.getMessage());
    }

    @Test
    void getResults_shouldReturnVotesPerParty_whenReadResultsSucceeds() {
        VoteService spyService = spy(voteService);
        doReturn(true).when(spyService).readResults(anyString(), anyString(), eq("2021"));

        Vote v1 = new Vote("Partij A", 1000, "2021");
        Vote v2 = new Vote("Partij B", 500, "2021");

        spyService.add("2021", v1);
        spyService.add("2021", v2);

        Map<String, Integer> result = spyService.getResults("2021");

        assertEquals(2, result.size());
        assertEquals(1000, result.get("Partij A"));
        assertEquals(500, result.get("Partij B"));
    }

    @Test
    void getFileNameForYear_shouldReturnCorrectFilename_whenYearIs2021()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        NationalVotesRepo mockRepo = mock(NationalVotesRepo.class);
        VoteService voteService = new VoteService(mockRepo);

        String result = voteService.getClass().getDeclaredMethod("getFileNameForYear", String.class)
                .invoke(voteService, "2021").toString();

        assertEquals("Totaaltelling_TK2021.eml.xml", result);
    }


    @Test
    void add_shouldStoreVoteUnderCorrectYear() {
        NationalVotesRepo mockRepo = mock(NationalVotesRepo.class);
        VoteService voteService = new VoteService(mockRepo);

        Vote vote = new Vote("Partij X", 1234, "2025");
        voteService.add("2025", vote);

        Map<String, Integer> result = voteService.getVotesPerParty("2025");

        assertEquals(1, result.size());
        assertEquals(1234, result.get("Partij X"));
    }

    @Test
    void readResults_shouldReturnTrue_whenVotesAlreadyExistInRepo() {
        NationalVotesRepo mockRepo = mock(NationalVotesRepo.class);
        VoteService voteService = new VoteService(mockRepo);

        List<Vote> existingVotes = List.of(new Vote("Partij X", 1000, "2021"));
        when(mockRepo.findAllByYear("2021")).thenReturn(existingVotes);

        boolean result = voteService.readResults("irrelevant", "irrelevant.xml", "2021");

        assertTrue(result);
    }

    @Test
    void readResults_shouldReturnFalse_whenXmlFileNotFound() {
        NationalVotesRepo mockRepo = mock(NationalVotesRepo.class);
        VoteService voteService = new VoteService(mockRepo);

        when(mockRepo.findAllByYear("2099")).thenReturn(List.of());

        boolean result = voteService.readResults("folderBestaatNiet", "bestandBestaatNiet.xml", "2099");

        assertFalse(result);
    }

    @Test
    void getVotesPerParty_shouldReturnCorrectVoteSumsForEachParty() {
        NationalVotesRepo mockRepo = mock(NationalVotesRepo.class);
        VoteService voteService = new VoteService(mockRepo);

        Vote v1 = new Vote("Partij A", 100, "2021");
        Vote v2 = new Vote("Partij B", 200, "2021");
        Vote v3 = new Vote("Partij A", 300, "2021");

        voteService.add("2021", v1);
        voteService.add("2021", v2);
        voteService.add("2021", v3);

        Map<String, Integer> result = voteService.getVotesPerParty("2021");

        assertEquals(2, result.size());
        assertEquals(400, result.get("Partij A"));
        assertEquals(200, result.get("Partij B"));
    }

    @Test
    void getSortedResults_shouldSortByVotesDescending() {
        NationalVotesRepo mockRepo = mock(NationalVotesRepo.class);
        VoteService voteService = spy(new VoteService(mockRepo));

        Map<String, Integer> mockVotes = Map.of(
                "Partij A", 100,
                "Partij B", 300,
                "Partij C", 200
        );
        doReturn(mockVotes).when(voteService).getResults("2021");

        Map<String, Integer> result = voteService.getSortedResults("2021", "votes-desc");

        assertEquals(List.of("Partij B", "Partij C", "Partij A"), result.keySet().stream().toList());
    }

    @Test
    void getSortedResults_shouldSortByNameAscending() {
        NationalVotesRepo mockRepo = mock(NationalVotesRepo.class);
        VoteService voteService = spy(new VoteService(mockRepo));

        Map<String, Integer> mockVotes = Map.of(
                "Z-Partij", 50,
                "A-Partij", 150,
                "M-Partij", 100
        );
        doReturn(mockVotes).when(voteService).getResults("2021");

        Map<String, Integer> result = voteService.getSortedResults("2021", "name-asc");

        assertEquals(List.of("A-Partij", "M-Partij", "Z-Partij"), result.keySet().stream().toList());
    }

}
