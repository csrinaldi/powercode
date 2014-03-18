package com.workingflow.common.repository;

import com.google.common.base.Optional;

import com.google.common.collect.Iterables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación base para los {@link Repository}
 *
 * @param <T> tipo de la entidad
 * @param <ID> tipo del ID
 * 
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 * 
 */
public abstract class AbstractRepository<T, ID extends Serializable> implements Repository<T, ID> {

    private final Class<T> entityType;

    protected AbstractRepository(Class<T> entityType) {
        this.entityType = entityType;
    }

    @SuppressWarnings("unchecked")
    protected AbstractRepository() {
        entityType = Repositories.entityType((Class<? extends Repository<T, ID>>) getClass());
    }

    @Override
    public Class<T> entityType() {
        return entityType;
    }

    @Override
    public T find(ID id) {
        return findById(id).orNull();
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        final ArrayList<T> all = new ArrayList<T>();
        for (final ID id : ids) {
            all.add(find(id));
        }
        return all;
    }

    @Override
    public T findUnique(Specification<? super T>... specs) {
        return find(specs).orNull();
    }

    @Override
    public T findUnique(Iterable<Specification<? super T>> specs) {
        return find(specs).orNull();
    }

    @Override
    public Optional<T> find(Iterable<Specification<? super T>> specs) {
        return find(Iterables.toArray(specs, Specification.class));
    }

    @Override
    public List<T> findAll(Iterable<Specification<? super T>> specs) {
        return findAll(Iterables.toArray(specs, Specification.class));
    }

    @Override
    public List<T> findAll(int offset, int limit, Iterable<Specification<? super T>> specs) {
        return findAll(offset, limit, Iterables.toArray(specs, Specification.class));
    }

    @Override
    public boolean exists(ID id) {
        return findById(id).isPresent();
    }
}
