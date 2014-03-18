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

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.typesafe.config.ConfigFactory;
import com.workingflow.akka.actors.DeliveryActor;
import com.workingflow.akka.commons.KernelBootable;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * Documentación de {@link KernelBootableImpl}.
 *
 * //TODO
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
@Singleton
public class KernelBootableImpl implements KernelBootable {

    private final ActorSystem system;

    public KernelBootableImpl() {
        
        this.system = ActorSystem.create("server-akka", ConfigFactory.load("scit-conf"));
    }

    @Override
    public void startup() {
        //Delivery Actor
        ActorRef delivery = system.actorOf(Props.create(DeliveryActor.class), "delivery");
        system.eventStream().subscribe(delivery, Delivery.class);
        
        //Listening Actor
        system.actorOf(Props.create(ListeningActor.class), "listening");
    }

    @Override
    public void shutdown() {
        system.shutdown();
    }
    
    @Produces
    @Singleton
    public ActorSystem getActorSystem(){
        return system;
    }

}
