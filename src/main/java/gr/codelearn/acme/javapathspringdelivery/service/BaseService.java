package gr.codelearn.acme.javapathspringdelivery.service;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BaseService<T, K> {
    List<T> createAll(final T... items);

    List<T> createAll(final List<T> items);
    T create(final T item);

    void update(T item);

    void delete(T item);

    void deleteById(K id);

    boolean exists(T item);

    T get(K id);

    @TimeLimiter(name = "basicTimeout")
    CompletableFuture<List<T>> findAll();

    Long count();
}