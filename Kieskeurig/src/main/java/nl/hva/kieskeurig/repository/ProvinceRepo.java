package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepo extends JpaRepository<Province, Integer> {
    Province findByName(String name);
}
