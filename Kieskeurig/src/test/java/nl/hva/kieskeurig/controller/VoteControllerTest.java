package nl.hva.kieskeurig.controller;


import nl.hva.kieskeurig.service.VoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class VoteControllerTest {

    @Mock
    private VoteService voteService;

    @InjectMocks
    private VoteController voteController;

    @Test
    void testGetVotesPerParty() {

        Map<String, Integer> expectedVotes = Map.of("Party A", 1200, "Party B", 8000);

        when(voteService.getResults("2023")).thenReturn(expectedVotes);

        Map<String, Integer> actualResults = voteController.getVotesPerParty("2023");

        assertEquals(expectedVotes, actualResults);


    }
}
