package nl.hva.kieskeurig.controller;

import nl.hva.kieskeurig.model.Vote;
import nl.hva.kieskeurig.repository.NationalVotesRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NationalVotesRepo voteRepo;

    @BeforeEach
    void setUp() {
        voteRepo.deleteAll();
        voteRepo.saveAll(List.of(
                new Vote("Partij A", 1000, "2023"),
                new Vote("Partij B", 800, "2023")
        ));
    }

    @Test
    void getVotesPerParty_shouldReturnSortedResults_withStatus200() throws Exception {
        mockMvc.perform(get("/api/xml/votes/parties")
                        .param("year", "2023")
                        .param("sort", "votes-desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Partij A']").value(1000))
                .andExpect(jsonPath("$.['Partij B']").value(800));
    }

    @Test
    void getVotesPerParty_shouldReturn400_whenYearIsInvalid() throws Exception {
        mockMvc.perform(get("/api/xml/votes/parties")
                        .param("year", "1999")
                        .param("sort", "none")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
