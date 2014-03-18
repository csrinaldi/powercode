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
package com.workingflow.console.actor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.workingflow.akka.commons.cdi.AwareActor;
import com.workingflow.akka.commons.rabbit.RabbitProviderConfig;
import com.workingflow.akka.console.CommandConsole;
import com.workingflow.share.protobuff.Protocol;
import com.workingflow.share.protobuff.Protocol.Response;
import javax.inject.Inject;

/**
 * Documentación de {@link DeliveryActor}.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
public class DeliveryActor extends AwareActor {

    @Inject
    RabbitProviderConfig providerConf;

    @Inject
    CommandConsole console;

    private Channel channel;

    @Override
    public void preStart() {
        super.preStart(); //To change body of generated methods, choose Tools | Templates.
        channel = providerConf.getChannel();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        boolean publish = false;
        String uuid;
        if (message instanceof Protocol.MetaRequest) {
            Protocol.MetaRequest req = (Protocol.MetaRequest) message;
            if (req.isInitialized()) {
                if (req.hasConnect()) {
                    publish = req.getConnect().isInitialized();
                    uuid = req.getConnect().getRequest().getUuid();
                } else {
                    publish = req.getCommand().isInitialized();
                    uuid = req.getCommand().getRequest().getRequest().getUuid();
                }

                if (publish) {
                    console.writeResult("Command Send with UUID: "+uuid, false);
                    channel.basicPublish("", RabbitProviderConfig.COMMAND_QUEUE, null, req.toByteArray());
                } else {
                    console.writeResult("Command not Send", false);
                }
            }
            
        } else if (message instanceof QueueingConsumer.Delivery) {
            QueueingConsumer.Delivery d = (QueueingConsumer.Delivery) message;
            Protocol.MetaResponse metaResponse = Protocol.MetaResponse.parseFrom(d.getBody());
            if (metaResponse.hasConnectResponse()) {
                console.setUser(metaResponse.getConnectResponse().getUser());
                console.setLogged(true);
                console.setToken(metaResponse.getConnectResponse().getToken());
                console.writeResult(metaResponse.getConnectResponse().getResponse().getStatus().toString(), true);
            }else if( metaResponse.hasResponse() ){
                Response response = metaResponse.getResponse();
                console.writeResult(response.getStatus().toString(), true );
            }
        } else {
            unhandled(message);
        }
    }
}
