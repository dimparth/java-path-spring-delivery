package gr.codelearn.acme.javapathspringdelivery.service;

import java.util.List;

public interface BaseService<T, K> {
    List<T> createAll(final T... items);

    List<T> createAll(final List<T> items);
    T create(final T item);

    void update(T item);

    void delete(T item);

    void deleteById(K id);

    boolean exists(T item);

    T get(K id);

    List<T> findAll();

    Long count();
}