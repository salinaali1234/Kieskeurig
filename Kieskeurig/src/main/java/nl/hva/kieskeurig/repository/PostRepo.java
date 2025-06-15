package nl.hva.kieskeurig.repository;

import nl.hva.kieskeurig.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {
    // You don't need to add anything here unless you want custom queries
}
