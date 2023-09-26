package gr.codelearn.acme.javapathspringdelivery.service;

import java.util.List;

public interface BaseService<T, K> {
    T create(final T item);

    void update(T item);

    void delete(T item);

    void deleteById(K id);

    boolean exists(T item);

    T get(K id);

    List<T> findAll();

    Long count();
}