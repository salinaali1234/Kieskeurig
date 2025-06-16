package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepo extends JpaRepository<Province, Integer> {
    boolean existsBy();

    @Query("SELECT p FROM Province p LEFT JOIN FETCH p.constituencies WHERE p.name = :name")
    Optional<Province> findByName(@Param("name") String name);

    @Query("SELECT p FROM Province p LEFT JOIN FETCH p.constituencies WHERE p.id = :id")
    Optional<Province> findById(@Param("id") int id);

    Province findTopByOrderByIdDesc();
}
