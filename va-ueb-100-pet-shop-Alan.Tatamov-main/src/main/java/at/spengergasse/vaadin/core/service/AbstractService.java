package at.spengergasse.vaadin.core.service;


import at.spengergasse.vaadin.core.domain.CreateInstanceFunction;
import at.spengergasse.vaadin.core.domain.Entity;
import at.spengergasse.vaadin.core.exception.ServiceException;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author der SLM
 */
@Slf4j
public abstract class AbstractService<T extends Entity<Long>> implements Service<T, Long> {
    private final AtomicLong sequence = new AtomicLong(5000);
    private final List<T> data = new ArrayList<>();

    protected AbstractService() {
    }

    protected AbstractService(int count, CreateInstanceFunction<T, Long> fx) {
        try {
            Faker faker = new Faker();
            for (int i = 0; i < count; i++) {
                data.add(fx.create(sequence::getAndIncrement, faker));
            }
            log.debug("created data: {}", data.size());
        } catch (Exception e) {
            log.error("Error creating data. {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<T> findById(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id must not be null");
        Optional<T> bean =  this.data.stream()
                .filter(entity -> id.equals(entity.getId()))
                .findFirst();
        return bean
                .map(SerializationUtils::clone)
                .or(Optional::empty);
    }

    @Override
    public Collection<T> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Collection<T> findAll(Comparator<T> sort) {
        if (sort == null)
            throw new IllegalArgumentException("Sort must not be null");
        List<T> sorted = new ArrayList<>(data);
        sorted.sort(sort);
        return sorted;
    }


    // TODO: rename save to upsert
    @Override
    public boolean save(T entity) {
        return entity.getId() == null ? insert(entity) : update(entity);
    }

    @Override
    public boolean insert(T entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null!");
        if (entity.getId() != null)
            throw new IllegalArgumentException("Could not insert entity. Entity already has an id!");

        entity.setId(sequence.getAndIncrement());
        log.info("Objekt \"{}\" erfolgreich gespeichert", entity);
        return this.data.add(entity);
    }

    @Override
    public boolean update(T entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null!");
        if (entity.getId() == null)
            throw new IllegalArgumentException("Could not update entity. Entity already has no id!");

        data.removeIf(t -> entity.getId().equals(t.getId()));
        return data.add(entity);
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id must not be null!");
        return this.data.removeIf(entity -> id.equals(entity.getId()));
    }

    @Override
    public boolean deleteAll(Collection<T> data) {
        return this.data.removeAll(data);
    }
}
