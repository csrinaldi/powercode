package com.workingflow.common.domain;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Clase de utilidad para trabajar con {@link Persistable}
 *
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 */
public class Persistables {

    private Persistables() {
    }
    private static final Function<Persistable<?>, Serializable> GET_ID_FUNCTION = new Function<Persistable<?>, Serializable>() {
        @Override
        public Serializable apply(Persistable<?> persistable) {
            return persistable.getId();
        }
    };

    /**
     * Funcion que devuelve el ID de una entidad
     *
     * @param <ID> el tipo del ID
     * @param <P> el tipo de la entidad
     * @return el id
     */
    public static <ID extends Serializable, P extends Persistable<ID>> Function<P, ID> getIdFunction() {
        return (Function) GET_ID_FUNCTION;
    }

    private static class IdComparator<ID extends Serializable & Comparable<ID>, P extends Persistable<ID>> implements Comparator<P> {

        private IdComparator() {
        }

        @Override
        public int compare(P a, P b) {
            return a.getId().compareTo(b.getId());
        }
    }
    private final static IdComparator ID_COMPARATOR = new IdComparator();

    /**
     * Comparador de identificadores
     */
    public static <ID extends Serializable & Comparable<ID>, P extends Persistable<ID>> Comparator<P> idComparator() {
        return ID_COMPARATOR;
    }

    public static <ID extends Serializable & Comparable<ID>, P extends Persistable<ID>> Predicate<P> hasIdPredicate(final ID id) {
        return new Predicate<P>() {
            @Override
            public boolean apply(P persistable) {
                return persistable == null ? false : id.equals(persistable.getId());
            }
        };
    }
    private final static Predicate<Persistable<?>> IS_NEW_PREDICATE = new Predicate<Persistable<?>>() {
        @Override
        public boolean apply(Persistable<?> p) {
            return p.isNew();
        }
    };

    public static Predicate<Persistable<?>> isNewPredicate() {
        return (Predicate) IS_NEW_PREDICATE;
    }
}
