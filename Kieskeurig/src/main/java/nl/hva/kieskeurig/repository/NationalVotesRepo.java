package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface NationalVotesRepo extends JpaRepository<Vote, Long> {
    Optional<Vote> findByPartyNameAndYear(String partyName, String year);

}
