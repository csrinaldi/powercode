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
package com.workingflow.akka.commons.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Documentación de {@link RabbitProviderConfig}.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
@Singleton
public class RabbitProviderConfig {

    public static String COMMAND_QUEUE = "COMMAND_QUEUE";
    public static String RESPONSE_QUEUE = "RESPONSE_QUEUE";

    private final ConnectionFactory factory;
    private Channel channel;
    private Connection connection;

    @Inject
    public RabbitProviderConfig(ConnectionFactory factory) {
        this.factory = factory;
    }
    
    @PostConstruct
    public void start() {
        try {
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();
            
            channel.queuePurge(COMMAND_QUEUE);
            channel.queuePurge(RESPONSE_QUEUE);
            
            channel.queueDeclare(
                    COMMAND_QUEUE,
                    true,
                    false,
                    false,
                    null);
            channel.queueDeclare(
                    RESPONSE_QUEUE,
                    true,
                    false,
                    false,
                    null);
        } catch (IOException ex) {
            Logger.getLogger(RabbitProviderConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    public void stop() {
        try {
            channel.queueDelete(COMMAND_QUEUE);
            channel.queueDelete(RESPONSE_QUEUE);
            channel.close();
        } catch (IOException ex) {
            Logger.getLogger(RabbitProviderConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Channel getChannel(){
        return channel;
    }
    
    public QueueingConsumer getConsumer(){
        return new QueueingConsumer(channel);
    }
}
