package nl.hva.kieskeurig.repository.ConstituencyRepo;

import nl.hva.kieskeurig.model.Constituency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConstituencyRepository extends JpaRepository<Constituency, Integer> {
    boolean existsBy();

    Optional<Constituency> findById(int id);
}
