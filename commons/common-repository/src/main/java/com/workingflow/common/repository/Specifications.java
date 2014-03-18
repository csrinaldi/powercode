package com.workingflow.common.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

/**
 *
 * Utilidades para especificaciones. Contiene operadores como AND y OR.
 * 
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 * @param <T>
 * 
 */
public class Specifications<T> implements Specification<T> {

    static class EqualsSpecification<X, Y> implements Specification<X> {

        private final SingularAttribute<? super X, Y> attribute;
        private final Y value;

        public EqualsSpecification(SingularAttribute<? super X, Y> attribute, Y value) {
            
            this.attribute = attribute;
            this.value = value;
            System.out.println("Atribute "+this.attribute.getName());
            System.out.println("Atribute "+this.value.toString());
        }

        @Override
        public Predicate toPredicate(Root<? extends X> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            System.out.println("Root    "+root.toString());
            return builder.equal(root.get(attribute), value);
        }
    }
    
    static class LikeSpecification<X, Y> implements Specification<X> {

        private final SingularAttribute<? super X, String> attribute;
        private final String value;

        public LikeSpecification(SingularAttribute<? super X, String> attribute, String value) {
            this.attribute = attribute;
            this.value = value;
        }

        @Override
        public Predicate toPredicate(Root<? extends X> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            return builder.like(root.get(attribute), value + "%");
        }
    }
    
    private final Specification<T> spec;

    Specifications(Specification<T> spec) {
        this.spec = spec;
    }

    @Override
    public Predicate toPredicate(Root<? extends T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return spec.toPredicate(root, query, builder);
    }

    public Specifications<T> or(final Specification<? super T> other) {
        return new Specifications<>(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<? extends T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.or(spec.toPredicate(root, query, builder), other.toPredicate(root, query, builder));
            }
        });
    }

    public Specifications<T> and(final Specification<? super T> other) {
        return new Specifications<>(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<? extends T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.and(spec.toPredicate(root, query, builder), other.toPredicate(root, query, builder));
            }
        });
    }

    public static <T> Specifications<T> where(final Specification<T> spec) {
        return new Specifications<>(spec);
    }

    public static <T> Specifications<T> not(final Specification<? super T> spec) {
        return new Specifications<>(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<? extends T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.not(spec.toPredicate(root, query, builder));
            }
        });
    }

    public static <X, Y> Specification<X> equals(SingularAttribute<? super X, Y> attribute, Y value) {
        return new EqualsSpecification<>(attribute, value);
    }
    
    public static <X, Y> Specification<X> like(SingularAttribute<? super X, String> attribute, String value) {
        return new LikeSpecification<>(attribute, value);
    }

    public static <X, Y> Specification<X> isNull(final SingularAttribute<? super X, Y> attribute) {
        return new Specification<X>() {
            @Override
            public Predicate toPredicate(Root<? extends X> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.isNull(root.get(attribute));
            }
        };
    }

    public static <X, Y> Specification<X> isNotNull(final SingularAttribute<? super X, Y> attribute) {
        return new Specification<X>() {
            @Override
            public Predicate toPredicate(Root<? extends X> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.isNotNull(root.get(attribute));
            }
        };
    }

    public static <X> Specification<X> isTrue(final SingularAttribute<? super X, Boolean> attribute) {
        return new Specification<X>() {
            @Override
            public Predicate toPredicate(Root<? extends X> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.isTrue(root.get(attribute));
            }
        };
    }

    public static <X> Specification<X> isFalse(final SingularAttribute<? super X, Boolean> attribute) {
        return new Specification<X>() {
            @Override
            public Predicate toPredicate(Root<? extends X> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.isFalse(root.get(attribute));
            }
        };
    }
}
