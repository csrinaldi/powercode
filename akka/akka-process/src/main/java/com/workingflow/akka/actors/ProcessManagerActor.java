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
package com.workingflow.akka.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.common.base.Optional;
import com.workingflow.akka.commons.cdi.AwareActor;
import com.workingflow.share.protobuff.Protocol;
import com.workingflow.share.protobuff.Protocol.Command;
import com.workingflow.share.protobuff.Protocol.Command.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

/**
 * Documentación de {@link ProcessManagerActor}.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
public class ProcessManagerActor extends AwareActor {
    
    public static String parameterConverter(List<Protocol.Command.Parameter> params){
        String toReturn = "";
        for (Protocol.Command.Parameter parameter : params) {
            toReturn += "|"+parameter.getName()+":"+parameter.getValue();
        }
        
        return toReturn;
    }

    @Inject
    ActorSystem actorSystem;

    ActorRef singletonImportationProcess;

    @Override
    public void preStart() {
        super.preStart();
        singletonImportationProcess = actorSystem.actorOf(Props.create(ImportationProcess.class));//.withMailbox("prio-mailbox"));
    }

    void fireProcess(Command cmd, boolean isForked) {
        switch (cmd.getCommand()) {
            case "import":
                System.out.println("Lanzando Actores comando: "+cmd.getCommand());
                if ( isForked )
                    actorSystem.actorOf(Props.create(ImportationProcess.class)).tell(cmd, getSelf());
                else
                    singletonImportationProcess.tell(cmd, getSelf());
                break;
            case "export":
            case "fito":
            default:
                break;
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Protocol.Command) {
            Thread.sleep(15000);
            
            Command cmd = (Command) message;
            System.out.println("Procesando comando: "+cmd.getCommand());
            List<Command.Parameter> params = cmd.getParamsList();
            Optional<List<String>> factors = findFactorsParams(params);
            Boolean fork = findParalelParam(params);

            if (fork) {
                if (factors.isPresent()) {
                    List<String> list = factors.get();
                    for (String string : list) {
                        Command cm = replaceFactorParam(cmd, string);
                        fireProcess(cm, true);
                    }
                } else {
                    fireProcess(cmd, true);
                }
            } else {
                fireProcess(cmd, false);
            }
        } else {
            unhandled(message);
        }
    }

    /**
     * Método:
     * {@link ProcessManagerActor#replaceFactorParam(ar.gov.santafe.scit.share.protobuff.Protocol.Command, java.lang.String)}.
     *
     * Reemplaza el valor del parámetro factor en el comando. Este método es
     * utilizado cuando se hace <b>fork</b> del discriminante enviado en el
     * parametro <b>factor<b>.
     * <br/>Entonces cada comando enviado al subproceso creado tiene un unico
     * valor a procesar en el discriminante.
     * <br/>Ejemplo:
     *
     * <b>factor:[0201,0502,0120]</b>
     * <ul>
     * <li>1) Parameter[factor:0201] Subproceso 1</li>
     * <li>2) Parameter[factor:0502] Subproceso 2</li>
     * <li>3) Parameter[factor:0120] Subproceso 3</li>.
     * </ul>
     * <br/>
     *
     * El factor es un discriminante entendido por el subproceso, no es función
     * el manejador de procesos entender la semántica del factor discriminande,
     * este puede tener lógica inserta entendible por el proceso que lo
     * interpreta.
     *
     * <br/>
     * Por ejemplo:
     * <ul>
     * <li>factor:[1603,1011, {P - [1603,1011]}]</li>
     * <li>factor:[1603]</li>
     * </ul>
     * 
     * Por ejemplo: La ejecucion del siguiente comando:
     * 
     * {@code 
     *  import factor:[1603,1011, {P - [1603,1011]}] fork:true type:parcela priority:2
     * }
     * 
     * es pseudo-transformado en diferentes comandos y enviados al proceso relacionado
     * con el comando import:
     *     
     * {@code 
     * import factor:[1603] type:parcela priority:2
     * import factor:[1011] type:parcela priority:2
     * import factor:[{P - [1603,1011]}] type:parcela priority:2
     * }
     *
     * @param cmd
     * @param factorValue
     * @return
     */
    private Command replaceFactorParam(Command cmd, String factorValue) {
        List<Parameter> params = cmd.getParamsList();
        List<Parameter> target = new ArrayList<>();
        for (Parameter parameter : params) {
            if (parameter.getName().equals("factor")) {
                target.add(parameter.toBuilder().setValue(factorValue).build());
            } else if (!parameter.getName().equals("fork")) {
                target.add(parameter);
            }
        }

        return cmd.toBuilder().clearParams().addAllParams(target).build();

    }

    /**
     * Cada proceso puede representado por un comando, puede ser corrido en
     * paralelo para cada factor discriminante en el caso que exista dicho
     * factor. En el caso de que fork sea false (valor de defecto asumido) los
     * discriminantes son enviados al proceso (Actor) y este los procesará a su
     * manera.
     *
     * @param params
     * @return
     */
    private Boolean findParalelParam(List<Command.Parameter> params) {
        for (Command.Parameter parameter : params) {
            if (parameter.getName().equals("fork")) {
                return parameter.getValue().equals("true");
            }
        }
        return false;
    }

    /**
     * Retorna un mapa con el valor del factor y la posicion del parametro
     * factor
     *
     * @param params
     * @return
     */
    private Optional<List<String>> findFactorsParams(List<Command.Parameter> params) {
        int idx = 0;
        for (Command.Parameter parameter : params) {
            if (parameter.getName().equals("factor")) {
                String value = parameter.getValue();
                if (value.startsWith("[") && value.endsWith("]")) {
                    value = value.replace('[', ' ');
                    value = value.replace(']', ' ');
                    return Optional.of(Arrays.asList(value.split(",")));
                }
            }
        }
        return Optional.absent();
    }

    private String findTypeParams(List<Command.Parameter> params) {
        for (Command.Parameter parameter : params) {
            if (parameter.getName().equals("type")) {
                return parameter.getValue();
            }
        }
        return null;
    }

}
