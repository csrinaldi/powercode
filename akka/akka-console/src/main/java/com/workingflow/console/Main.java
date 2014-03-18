package com.workingflow.console;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import com.workingflow.akka.commons.KernelBootable;

/**
 * Prototype of Akka Console.
 *
 * Connect to platform across socket and send command for actor manipulation.
 */
public class Main {

    public static void main(String[] args) {
        Weld weld = new Weld();
        //weld.addExtension(new RepositoryExtension());
        WeldContainer container = weld.initialize();
        BeanManager bm = container.getBeanManager();
        Bean<KernelBootable> bean = (Bean<KernelBootable>) bm.getBeans(KernelBootable.class).iterator().next();
        CreationalContext<KernelBootable> ctx = bm.createCreationalContext(bean);
        KernelBootable boot = (KernelBootable) bm.getReference(bean, KernelBootable.class, ctx);
        boot.startup();
        System.out.println("Scit Akka Console is started ....\n");
    }

}
