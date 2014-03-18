package com.workingflow.common.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 
 * Definición del patrón de especificaciones para JPA.
 * 
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 * 
 */
public interface Specification<T> {
    Predicate toPredicate(Root<? extends T> root, CriteriaQuery<?> query,
            CriteriaBuilder builder);
}
