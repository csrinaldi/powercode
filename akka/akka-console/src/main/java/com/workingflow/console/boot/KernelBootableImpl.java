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
package com.workingflow.console.boot;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.workingflow.console.actor.DeliveryActor;
import com.rabbitmq.client.QueueingConsumer;
import com.workingflow.akka.commons.KernelBootable;
import com.workingflow.akka.console.CommandConsole;
import com.workingflow.share.protobuff.Protocol;

/**
 * Documentación de {@link KernelBootableImpl}.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
@Singleton
public class KernelBootableImpl implements KernelBootable {

    @Inject
    private ActorSystem system;
    
    @Inject
    private CommandConsole commandConsole;
    
    public UUID clientId;

    @Override
    public void startup() {

        ActorRef delivery = system.actorOf(Props.create(DeliveryActor.class), "delivery");
        system.eventStream().subscribe(delivery, Protocol.MetaRequest.class);
        system.eventStream().subscribe(delivery, QueueingConsumer.Delivery.class);
        
        //Listening Actor
        system.actorOf(Props.create(ListeningActor.class), "listening");
        clientId = UUID.randomUUID();
        commandConsole.setClientId(clientId);

        commandConsole.run();
    }

    @Override
    public void shutdown() {
        system.shutdown();
    }
}
