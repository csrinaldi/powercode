package com.workingflow.common.repository.jpa;

/**
 * Interface que garantiza una transacción al ejecutar el método 
 * {@link Transactor#perform(Transactor.Execution) }.
 *
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 */
public interface Transactor {

    /**
     * Ejecución a realizar dentro de una transacción.
     * 
     */
    interface Execution<T> {

        /**
         * Este método es invocado por el {@link Transactor} durante una
         * transacción.
         * @return el resultado de la transacción
         */
        T execute();
    }

    /**
     * Este método se invoca explícitamente y su ejecución está garantizada
     * a desarrollarse dentro de una transacción.
     * 
     * @param e ejecución
     * @return resultado de la transacción
     */
    <T> T perform(Execution<T> e);
}
