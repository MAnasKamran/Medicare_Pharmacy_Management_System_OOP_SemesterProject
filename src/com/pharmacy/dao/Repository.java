package com.pharmacy.dao;

import com.pharmacy.model.Entity;
import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD repository interface.
 * OOP: Generics, Interface, Bounded Type Parameters
 *
 * @param <T>  Entity type
 * @param <ID> Primary key type
 */
public interface Repository<T extends Entity, ID> {

    /** Save a new entity (assigns an ID). */
    T save(T entity);

    /** Update an existing entity. */
    void update(T entity);

    /** Find by primary key. */
    Optional<T> findById(ID id);

    /** Return all entities. */
    List<T> findAll();

    /** Hard-delete an entity by ID. */
    boolean deleteById(ID id);

    /** Total count of persisted entities. */
    int count();
}
