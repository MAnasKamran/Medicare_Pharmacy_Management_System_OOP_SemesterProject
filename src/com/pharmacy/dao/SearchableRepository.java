package com.pharmacy.dao;

import com.pharmacy.model.Entity;
import com.pharmacy.model.Searchable;
import java.util.List;

/**
 * Extended repository that adds keyword search.
 * OOP: Interface Inheritance, Generics with multiple bounds
 *
 * @param <T> Entity type that is also Searchable
 */
public interface SearchableRepository<T extends Entity & Searchable>
        extends Repository<T, Integer> {

    /** Return entities whose matches() method returns true for the keyword. */
    List<T> search(String keyword);
}
