package com.workingflow.common.repository;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Provider;

/**
 * Esta clase unifica repositorios que extienden a una entidad de un determinado
 * tipo. Este ensamble es necesario debido a la particularidad de que JPA no 
 * soporta el operador UNION.
 * 
 * @author atesti
 * @param <T> Supertipo de la entidad a persistir
 * @param <ID> Tipo del identificador
 * @param <R>  Supertipo de los repositorios a unificar
 */
public class UnifiedRepository<T, ID extends Serializable, R extends Repository<? extends T, ID>>
        extends AbstractRepository<T, ID> implements Repository<T, ID> {

    private final Provider<? extends R>[] repositories;

    /**
     * 
     * @param repositories repositorios a unificar
     */
    protected UnifiedRepository(Provider<? extends R>... repositories) {
        this.repositories = repositories;
    }

    public UnifiedRepository(Class<T> entityType, Provider<? extends R>... repositories) {
        super(entityType);
        this.repositories = repositories;
    }

    @Override
    public void deleteBy(Specification<? super T>... specs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer save(Specification<? super T>... specs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected abstract class Folding<O> {

        public abstract O apply(O accumulator, R repository);
    }

    protected abstract class OptionalFolding<O> {

        public abstract Optional<O> apply(O accumulator, R repository);
    }

    protected abstract class ListMapping<O> {

        public abstract List<? extends O> apply(R repository);
    }

    protected abstract class Mapping<O> {

        public abstract O apply(R repository);
    }

    /**
     * Método de utilidad que permite iterar todos los repositorios.
     * 
     * @param mapping 
     */
    protected void foreach(Mapping<Boolean> mapping) {
        for (final Provider<? extends R> r : repositories) {
            if (!mapping.apply(r.get())) {
                break;
            }
        }
    }

    protected <O> O fold(@Nullable O initial, Folding<O> folding) {
        O accumulator = initial;
        for (final Provider<? extends R> r : repositories) {
            accumulator = folding.apply(accumulator, r.get());
        }
        return accumulator;
    }
    
    protected <O> List<O> flatMap(ListMapping<O> mapping) {
        final List<O> accumulator = new ArrayList<O>();
        for (final Provider<? extends R> r : repositories) {
            accumulator.addAll(mapping.apply(r.get()));
        }
        return accumulator;
    }

    @Override
    public Optional<T> findById(final ID id) {
        Optional<? extends T> result = Optional.absent();
        for (final Provider<? extends R> r : repositories) {
            result = r.get().findById(id);
            if (result.isPresent()) {
                break;
            }
        }
        return (Optional<T>) result;
    }

    @Override
    public <S extends T> S save(final S entity) {
        Preconditions.checkNotNull(entity);
        for (final Provider<? extends R> r : repositories) {
            final R repository = r.get();
            if (repository.entityType().isAssignableFrom(entity.getClass())) {
                return ((Repository<T, ID>) repository).save(entity);
            }
        }
        return null;
    }

    @Override
    public void delete(final T entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> findAll(final Specification<? super T>... specs) {
        return flatMap(new ListMapping<T>() {
            @Override
            public List<? extends T> apply(R repository) {
                return repository.findAll(specs);
            }
        });
    }

    @Override
    public Optional<T> find(final Specification<? super T>... specs) {
        Optional<? extends T> result = Optional.absent();
        for(final Provider<? extends R> r: repositories) {
            result = r.get().find(specs);
            if(result.isPresent()) {
                break;
            }
        }
        return (Optional<T>)result;
    }

    @Override
    public List<T> findAll() {
        return flatMap(new ListMapping<T>() {
            @Override
            public List<? extends T> apply(R repository) {
                return repository.findAll();
            }
        });
    }

    @Override
    public List<T> findAll(final int offset, final int limit) {
        return flatMap(new ListMapping<T>() {
            @Override
            public List<? extends T> apply(R repository) {
                return repository.findAll(offset, limit);
            }
        });
    }

    @Override
    public List<T> findAll(final int offset, final int limit, final Specification<? super T>... specs) {
        return flatMap(new ListMapping<T>() {
            @Override
            public List<? extends T> apply(R repository) {
                return repository.findAll(offset, limit, specs);
            }
        });
    }

    @Override
    public long count(final Specification<? super T>... specs) {
        return fold(0L, new Folding<Long>() {
            @Override
            public Long apply(Long accumulator, R repository) {
                return accumulator + repository.count(specs);
            }
        });
    }

    @Override
    public long count() {
        return fold(0L, new Folding<Long>() {
            @Override
            public Long apply(Long accumulator, R repository) {
                return accumulator + repository.count();
            }
        });
    }
}
