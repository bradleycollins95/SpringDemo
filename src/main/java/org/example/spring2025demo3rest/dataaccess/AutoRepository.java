package org.example.spring2025demo3rest.dataaccess;

import org.example.spring2025demo3rest.pojos.Auto;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for {@link Auto} entities.
 * Provides basic CRUD operations and custom query methods for Autos.
 */
public interface AutoRepository extends CrudRepository<Auto, Long> {
    /**
     * Retrieves all Auto records associated with a specific user ID.
     *
     * @param userId the ID of the user whose autos should be retrieved
     * @return an iterable collection of Autos linked to the given user
     */
    Iterable<Auto> getAllByUserId(Long userId);
}

