/*
 * Copyright 2013 Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.workingflow.akka.persistence.impl;


import com.workingflow.akka.commons.persistence.UnitOfWork;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;

/**
 * Documentación de {@link UnitOfWorkImpl}.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
@Dependent
public class UnitOfWorkImpl implements UnitOfWork {

    public static final ThreadLocal< EntityManager> em
            = new ThreadLocal< EntityManager>() {

                @Override
                protected EntityManager initialValue() {
                    EntityManager e = Resources.getEmf().createEntityManager();
                    System.out.println("Creando EntityManager.... OK " + e.toString() + "en Thread " + Thread.currentThread().getId());
                    return e;
                }

                @Override
                public void remove() {
                    EntityManager e = get();
                    if (e.isOpen()) {
                        System.out.println("Cerrando EntityManager ... OK " + e.toString() + "en Thread " + Thread.currentThread().getId());
                        if (e.getTransaction().isActive()) {
                            e.getTransaction().commit();
                            e.flush();
                        }
                        e.close();
                    }
                    super.remove();
                }
            };

    @Override
    public void begin() {
        if ( em.get() == null ){
            em.set(Resources.getEmf().createEntityManager());
        }

    }

    @Override
    public void end() {
        em.remove();
    }
    
    @Produces
    public EntityManager getManager(){
        return em.get();
    }
}
