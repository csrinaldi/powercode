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

import akka.japi.Procedure;
import com.workingflow.akka.commons.cdi.AwareActor;
import com.workingflow.akka.domain.Proceso;
import com.workingflow.akka.repository.ProcesoRepository;
import com.workingflow.akka.repository.specs.ProcesoSpecs;
import com.workingflow.share.protobuff.Protocol;
import com.workingflow.share.protobuff.Protocol.Command.Parameter;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Documentación de {@link ImportationProcess}.
 *
 * Proceso general de importacion, dependiendo del tipo, este cambia su
 * comportamiento para que el proceso se cumpla de manera satisfactoria.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
public class ImportationProcess extends AwareActor {

    @Inject
    Provider<ProcesoRepository> procesoRepository;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Protocol.Command) {
            Protocol.Command cmd = (Protocol.Command) message;
            String type = getType(cmd.getParamsList());
            List<Proceso> procesos = procesoRepository.get().findAll(ProcesoSpecs.exists(cmd.getCommand(), ProcessManagerActor.parameterConverter(cmd.getParamsList())));
            if (procesos.isEmpty()) {
                
                //TODO aplicar exepciones
                Proceso proceso = new Proceso();
                proceso.setComando(cmd.getCommand());
                proceso.setParameters(ProcessManagerActor.parameterConverter(cmd.getParamsList()));
                proceso.setEstado(Proceso.State.ACTIVE);
                procesoRepository.get().save(proceso);
                
                switch (type) {
                    case "parcela":
                        break;
                    case "manzana":
                        break;
                    case "zona":
                        break;
                    case "finca":
                        break;
                    default:
                        break;
                }

                //TODO verificar estado de terminacion
                //TODO verificar guardado en Base de Datos.
                proceso.setEstado(Proceso.State.COMPLETE);
                procesoRepository.get().save(proceso);
                
            }else{
                //TODO crear Request con error de repetido
            }
        }
    }

    Procedure<Protocol.Command> parcela = new Procedure<Protocol.Command>() {
        @Override
        public void apply(Protocol.Command message) {
        }
    };

    /**
     * Documentacion de metodo
     * {@link ImportationProcess#getType(java.util.List)}.
     *
     * Retorna el tipo de importacion para el proceso. Por ejemplo:
     *
     * <ul>
     * <li>parcela</li>
     * <li>manzana</li>
     * <li>zona</li>
     * <li>...</li>
     * </ul>
     *
     * @return
     */
    private String getType(List<Parameter> params) {
        for (Parameter param : params) {
            if (param.getName().equals("type")) {
                return param.getValue();
            }
        }
        return "";
    }
}
