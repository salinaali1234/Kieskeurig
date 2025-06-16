package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceVoteRepo extends JpaRepository<Vote, Long> {
    List<Vote> findAllByYear(String year);
}
