package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipalityRepo extends JpaRepository<Municipality, Integer> {
    boolean existsBy();
}
