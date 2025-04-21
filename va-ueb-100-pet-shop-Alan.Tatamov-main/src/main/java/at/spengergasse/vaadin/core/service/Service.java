package at.spengergasse.vaadin.core.service;

import at.spengergasse.vaadin.core.domain.Entity;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

/**
 * @author der SLM
 */
public interface Service<T extends Entity<I>, I> {
    Optional<T> findById(I id);

    Collection<T> findAll();

    Collection<T> findAll(Comparator<T> sort);

    boolean save(T entity);

    boolean insert(T entity);

    boolean update(T entity);

    boolean deleteById(I id);

    boolean deleteAll(Collection<T> selection);
}
