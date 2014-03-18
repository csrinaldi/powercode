package com.workingflow.akka.commons.cdi;

import akka.actor.UntypedActor;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * Documentacion sobre {@link AwareActor}.
 *
 * Permite que los miembros de la clase sean Inyectados utilizando CDI.
 *
 * @see BeanProvider
 *
 * @author Cristian Rinaldi
 */
public abstract class AwareActor extends UntypedActor {

    @Override
    public void postRestart(Throwable reason) {
        BeanProvider.injectFields(this);
    }

    @Override
    public void preStart() {
        BeanProvider.injectFields(this);
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }
}
