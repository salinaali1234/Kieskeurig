package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.VoteResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteResultRepo extends JpaRepository<VoteResult, Long> {
}
