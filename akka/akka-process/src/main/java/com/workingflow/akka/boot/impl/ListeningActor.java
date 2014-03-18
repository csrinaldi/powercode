/*
 * Copyright 2014 Workingflow.
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

import akka.actor.ActorSystem;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.workingflow.akka.commons.cdi.AwareActor;
import com.workingflow.akka.commons.rabbit.RabbitProviderConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 * Documentación de {@link ListeningActor}.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
public class ListeningActor extends AwareActor {

    @Inject
    private RabbitProviderConfig providerConfig;

    @Inject
    private ActorSystem actorSystem;

    private Channel channel;
    private QueueingConsumer consumer;


    @Override
    public void preStart() {
        //Necesario para la inyeccion de componentes
        super.preStart();
        try {
            channel = providerConfig.getChannel();
            consumer = providerConfig.getConsumer();
            
            channel.basicConsume(RabbitProviderConfig.COMMAND_QUEUE, consumer);
            while (true) {
                System.out.println("Listening for message in COMMAND_QUEUE .... ");
                QueueingConsumer.Delivery delivery = null;
                try {
                    delivery = consumer.nextDelivery();
                } catch (InterruptedException | ShutdownSignalException | ConsumerCancelledException ex) {
                    //TODO throw exception for control in Supervice Actor
                    Logger.getLogger(ListeningActor.class.getName()).log(Level.SEVERE, null, ex);
                }
                actorSystem.eventStream().publish(delivery);
            }

        } catch (IOException ex) {
            //TODO throw exception for control in Supervice Actor
            Logger.getLogger(ListeningActor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {

    }

}
