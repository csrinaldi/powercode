package com.workingflow.common.domain;

import java.io.Serializable;

/**
 *
 * Principal abstracción de datos persistente.
 *
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 *
 * @param <ID> Tipo del identificador
 */
public interface Persistable<ID extends Serializable> extends Serializable {

    /**
     * Devuelve el ID de la entidad
     * 
     * @return el id
     */
    ID getId();

    /**
     * Determina si la entidad es nueva
     * @return si la entidad es nueva
     */
    boolean isNew();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
