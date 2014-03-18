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

package com.workingflow.akka.boot.impl;

import com.workingflow.akka.commons.KernelBootable;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * Documentación de {@link AkkaMain}.
 * 
 * //TODO
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
public class AkkaMain {

    /**
     * 
     * 
     * @param args Argumentos de la lìnea de comandos.
     */
    public static void main(String[] args) {
        Weld weld = new Weld();
        //weld.addExtension(new RepositoryExtension());
        WeldContainer container = weld.initialize();
        BeanManager bm = container.getBeanManager();
        Bean<KernelBootable> bean = (Bean<KernelBootable>) bm.getBeans(KernelBootable.class).iterator().next();
        CreationalContext<KernelBootable> ctx = bm.createCreationalContext(bean);
        KernelBootable boot = (KernelBootable) bm.getReference(bean, KernelBootable.class, ctx);
        boot.startup();
        System.out.println("Scit Akka Platform is started and listening messages....");
    }
}
