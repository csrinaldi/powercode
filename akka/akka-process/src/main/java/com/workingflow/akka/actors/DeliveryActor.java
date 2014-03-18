/*
 * Copyright 2014 WorkingFlow.
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
package com.workingflow.akka.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.workingflow.akka.commons.cdi.AwareActor;
import com.workingflow.akka.commons.rabbit.RabbitProviderConfig;
import com.workingflow.share.protobuff.Protocol;
import com.workingflow.share.protobuff.Protocol.ConnectResponse;
import com.workingflow.share.protobuff.Protocol.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

/**
 * Documentación de {@link DeliveryActor}.
 * 
 * Este actor es encargado de procesar los mensajes entrantes, e insertar
 * en la cola de Rabbit los Responses.
 * Realiza el control si el usuario esta autenticado antes de despachar el mensaje
 * 
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
public class DeliveryActor extends AwareActor {

    @Inject
    RabbitProviderConfig providerConf;
    
    @Inject
    ActorSystem system;

    private Channel channel;
    
    ActorRef processActorRef;
    
    Map<String, String>  users = new HashMap<>();

    @Override
    public void preStart() {
        super.preStart(); //To change body of generated methods, choose Tools | Templates.
        channel = providerConf.getChannel();
        //TODO hacer esto con una cola con actores
        //TODO inyectar actor para desacoplar
        processActorRef= system.actorOf(Props.create(ProcessManagerActor.class), "processManagerActor");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        Protocol.MetaResponse.Builder metaReponse = Protocol.MetaResponse.newBuilder();

        if (message instanceof QueueingConsumer.Delivery) {
            QueueingConsumer.Delivery d = (QueueingConsumer.Delivery) message;
            Protocol.MetaRequest metaRequest = Protocol.MetaRequest.parseFrom(d.getBody());

            Response.Builder response = Response.newBuilder();
            response.setUuid(UUID.randomUUID().toString());

            System.out.println("Procesando Mensaje");

            if (metaRequest.hasConnect()) {
                Protocol.Connect connect = metaRequest.getConnect();
                String user = connect.getUser();
                String passwd = connect.getPasswd();
                String uuid = connect.getRequest().getUuid();
                String clientId = connect.getRequest().getClientId();

                ConnectResponse.Builder connectResponse = ConnectResponse.newBuilder();
                response.setUuidRelation(uuid);

                System.out.println("Mensaje connect: " + user + " " + passwd + " " + uuid);

                boolean auth = false;
                if (uuid != null && !uuid.isEmpty()) {
                    if (user != null && !user.isEmpty()
                            && passwd != null && !passwd.isEmpty()) {
                        if (user.equals("admin") && passwd.equals("admin")) {
                            String token = UUID.randomUUID().toString();
                            connectResponse.setToken(token);
                            connectResponse.setUser("admin");
                            response.setStatus(Response.Status.Accepted);
                            auth = true;
                            users.put(token, clientId);
                        }
                    }
                }
                if (!auth) {
                    response.setStatus(Response.Status.Unauthorized);
                    connectResponse.setToken("");
                }

                connectResponse.setResponse(response);
                metaReponse.setConnectResponse(connectResponse);
                channel.basicPublish("", RabbitProviderConfig.RESPONSE_QUEUE, null, metaReponse.build().toByteArray());
            } else if (metaRequest.hasCommand()) {
                Protocol.Command cmd = metaRequest.getCommand();
                Protocol.AuthenticatedRequest req = cmd.getRequest();
                
                if ( authenticateRequest(req) ){
                    processActorRef.tell( cmd, getSelf() );
                }else{
                    response.setStatus(Response.Status.Forbidden);
                    response.setUuidRelation(cmd.getRequest().getRequest().getUuid());
                    Protocol.MetaResponse.Builder meta = Protocol.MetaResponse.newBuilder();
                    meta.setResponse(response.build());
                    channel.basicPublish("", RabbitProviderConfig.RESPONSE_QUEUE, null, meta.build().toByteArray());
                }
            }
        } else {
            unhandled(message);
        }
    }
    
    /**
     * Documentación {@link DeliveryActor#authenticateRequest(ar.gov.santafe.scit.share.protobuff.Protocol.AuthenticatedRequest)}.
     * <br/>
     * Valida por cada request entrante las credenciales del usuario, token y clientId.
     * 
     * @param request
     * @return 
     */
    private Boolean authenticateRequest(Protocol.AuthenticatedRequest request){
        String token = request.getToken();
        String clientId = request.getRequest().getClientId();
        return ( users.get(token) != null && users.get(token).equals(clientId) );
    }
}
