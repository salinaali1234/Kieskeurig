package nl.hva.kieskeurig.controller;


import nl.hva.kieskeurig.model.Vote;
import nl.hva.kieskeurig.repository.NationalVotesRepo;
import nl.hva.kieskeurig.service.VoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NationalVotesRepo voteRepo;

    @Test
    void testGetVotesPerParty_2023() throws Exception {
        Vote vote = new Vote("Partij X", 1234, "2023");
        when(voteRepo.findAllByYear("2023")).thenReturn(List.of(vote));

        mockMvc.perform(get("/api/xml/votes/parties?year=2023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Partij X']").value(1234));
    }
}
