package nl.hva.kieskeurig.repository;

import java.util.List;
/**
 * Generic interface for basic CRUD operations on a repository of type E.
 *
 * @param <E> the entity type
 */
public interface EntityRepository<E> {

    /**
     * Retrieves all instances of the entity.
     *
     * @return a list of all entities
     */
    List<E> findAll();

    /**
     * Finds an entity by its ID.
     *
     * @param id the unique identifier
     * @return the entity, or null if not found
     */
    E findById(long id);

    /**
     * Saves the entity to the repository.
     * Creates a new entity if the ID is 0, otherwise updates the existing one.
     *
     * @param entity the entity to save
     * @return the saved entity
     */
    E save(E entity);

    /**
     * Deletes the entity with the given ID.
     *
     * @param id the ID of the entity to delete
     * @return the deleted entity or null if not found
     */
    E deleteById(long id);

    /**
     * Executes a named JPQL query with parameters.
     *
     * @param jpqlName the name of the JPQL query
     * @param params   the query parameters
     * @return a list of entities matching the query
     */
    List<E> findByQuery(String jpqlName, Object... params);
                                // finds all instances from a named jpql-query
}
