package com.workingflow.common.repository.jpa;

import com.workingflow.common.repository.AbstractRepository;
import com.workingflow.common.repository.Repository;
import com.workingflow.common.repository.Specification;
import com.google.common.base.Optional;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Implementación para {@link Repository} basado en JPA.
 *
 * @param <T> el tipo de la entidad.
 * @param <ID> el tipo del ID.
 *
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 *
 */
public abstract class JpaRepository<T, ID extends Serializable> extends AbstractRepository<T, ID> implements Repository<T, ID> {

    Provider<EntityManager> em = null;
    Provider<Transactor> transactor;

    @Override
    public Optional<T> findById(ID id) {
        return Optional.fromNullable(em().find(entityType(), id));
    }

    @Override
    public List<T> findAll(int offset, int limit) {
        final CriteriaQuery<T> criteria = createQuery();
        final Root<T> root = criteria.from(entityType());
        criteria.select(root);
        return em().createQuery(criteria).setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    @Override
    public <S extends T> S save(final S entity) {
        final Transactor trx = transactor();
        final EntityManager manager = em();
        return trx.perform(new Transactor.Execution<S>() {
            @Override
            public S execute() {
                manager.persist(entity);
                manager.flush();
                return entity;
            }
        });
    }

    @Override
    public Integer save(final Specification<? super T>... specs) {
        final Transactor trx = transactor();
        final EntityManager manager = em();

        return trx.perform(new Transactor.Execution<Integer>() {
            @Override
            public Integer execute() {
                try {
                    List<T> listToSave = findAll(specs);
                    for (T t : listToSave) {
                        manager.persist(t);
                    }
                    manager.flush();
                    return new Integer(listToSave.size());
                } catch (Exception e) {
                    return 0;
                }
            }
        });
    }

    @Override
    public void deleteBy(final Specification<? super T>... specs) {
        final EntityManager manager = em();
        transactor().perform(new Transactor.Execution<Boolean>() {
            @Override
            public Boolean execute() {
                List<T> listToDelete = findAll(specs);
                for (T t : listToDelete) {
                    manager.remove(t);
                }
                manager.flush();
                return Boolean.TRUE;
            }
        });
    }

    @Override
    public void delete(final T entity) {
        final EntityManager manager = em();
        transactor().perform(new Transactor.Execution<Boolean>() {
            @Override
            public Boolean execute() {
                manager.remove(entity);
                manager.flush();
                return Boolean.TRUE;
            }
        });
    }

    protected Transactor transactor() {
        Preconditions.checkNotNull(transactor, "Transactor is not initialized");
        return transactor.get();
    }

    protected EntityManager em() {
        Preconditions.checkNotNull(em.get(), "EntityManager is not initialized");
        return em.get();
    }

    @Override
    public long count() {
        final CriteriaBuilder cb = em().getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        final Root<T> self = cq.from(entityType());
        cq.select(cb.count(self));
        return em().createQuery(cq).getSingleResult();
    }

    @Override
    public long count(Specification<? super T>... specs) {
        final CriteriaBuilder cb = em().getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        final Root<T> self = cq.from(entityType());
        cq.select(cb.count(self)).where(createPredicates(self, cq, cb, specs));
        return em().createQuery(cq).getSingleResult();
    }

    protected CriteriaQuery<T> createQuery() {
        return em().getCriteriaBuilder().createQuery(entityType());
    }

    @Override
    public List<T> findAll() {
        final CriteriaQuery<T> cq = createQuery();
        final Root<T> self = cq.from(entityType());
        cq.select(self);
        return em().createQuery(cq).getResultList();
    }

    protected Predicate[] createPredicates(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder, Specification<? super T>[] specs) {
        final Predicate[] predicates = new Predicate[specs.length];
        for (int i = 0; i < specs.length; i++) {
            predicates[i] = specs[i].toPredicate(root, query, builder);
        }
        return predicates;
    }

    protected TypedQuery<T> createQuery(Specification<? super T>... specs) {
        final CriteriaBuilder cb = em().getCriteriaBuilder();
        final CriteriaQuery<T> cq = cb.createQuery(entityType());
        final Root<T> self = cq.from(entityType());
        cq.select(self).where(createPredicates(self, cq, cb, specs));
        return em().createQuery(cq);
    }

    @Override
    public List<T> findAll(Specification<? super T>... specs) {
        return createQuery(specs).getResultList();
    }

    @Override
    public List<T> findAll(int offset, int limit, Specification<? super T>... specs) {
        return createQuery(specs).setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    @Override
    public Optional<T> find(Specification<? super T>... specs) {
        try {
            return Optional.of(createQuery(specs).getSingleResult());
        } catch (NoResultException ex) {
            return Optional.absent();
        }catch ( Exception e){
            return Optional.absent();
        }
    }

    protected <X> Optional<X> singleResult(TypedQuery<X> query) {
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException ex) {
            return Optional.absent();
        }
    }

    protected <N extends Number> N sum(SingularAttribute<? super T, N> attribute, @Nullable N defaultValue, Specification<? super T>... specs) {
        final CriteriaBuilder cb = em().getCriteriaBuilder();
        final CriteriaQuery<N> cq = cb.createQuery(attribute.getType().getJavaType());
        final Root<T> self = cq.from(entityType());
        cq.select(cb.sum(self.get(attribute))).where(createPredicates(self, cq, cb, specs));
        final N result = em().createQuery(cq).getSingleResult();
        return result == null ? defaultValue : result;
    }

    protected <N extends Number> N sumAbs(SingularAttribute<? super T, N> attribute, @Nullable N defaultValue, Specification<? super T>... specs) {
        final CriteriaBuilder cb = em().getCriteriaBuilder();
        final CriteriaQuery<N> cq = cb.createQuery(attribute.getType().getJavaType());
        final Root<T> self = cq.from(entityType());
        cq.select(cb.sum(cb.abs(self.get(attribute)))).where(createPredicates(self, cq, cb, specs));
        final N result = em().createQuery(cq).getSingleResult();
        return result == null ? defaultValue : result;
    }
}
