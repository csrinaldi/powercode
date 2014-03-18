/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workingflow.common.repository.jpa.cdi;


import com.workingflow.common.repository.jpa.Transactor;
import com.workingflow.common.repository.jpa.Transactor.Execution;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 */

public class NonJtaTransactor implements Transactor{

    @Inject
    Provider<EntityManager> em;

    @Override
    public <T> T perform(Execution<T> e) {
        EntityTransaction et = em.get().getTransaction();
        T result;
        if (!et.isActive()) {
            try {
                et.begin();
                result = e.execute();
                et.commit();
            } catch (RuntimeException ex) {
                et.rollback();
                throw ex;
            }
        } else {
            result = e.execute();
        }

        return result;
    }

    public EntityManager getEm() {
        return em.get();
    }
    
    
}
