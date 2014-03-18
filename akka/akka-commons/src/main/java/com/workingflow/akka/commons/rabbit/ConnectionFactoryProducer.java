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

import com.rabbitmq.client.ConnectionFactory;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * Documentación de {@link ConnectionFactoryProducer}.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */


public class ConnectionFactoryProducer {

    @Produces
    @Singleton
    public ConnectionFactory getConnectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        return factory;
    }
}