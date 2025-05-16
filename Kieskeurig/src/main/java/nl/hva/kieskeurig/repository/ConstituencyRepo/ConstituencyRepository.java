package nl.hva.kieskeurig.repository.ConstituencyRepo;

import nl.hva.kieskeurig.model.Constituency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstituencyRepository extends JpaRepository<Constituency, Integer> {
}
