package at.spengergasse.vaadin.core.domain;

import com.github.javafaker.Faker;

import java.util.function.Supplier;

/**
 * @author der SLM
 */
@FunctionalInterface
public interface CreateInstanceFunction<T, I> {
    T create(Supplier<I> idSupplier, Faker faker);
}
