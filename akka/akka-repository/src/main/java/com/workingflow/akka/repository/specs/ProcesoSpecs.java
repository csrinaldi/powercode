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
package com.workingflow.akka.repository.specs;

import com.workingflow.akka.domain.Proceso;
import com.workingflow.akka.domain.Proceso_;
import com.workingflow.common.repository.Specifications;


/**
 * Documentación de {@link ProcesoSpecs}.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
public class ProcesoSpecs {

    //TODO preguntar por el User....
    public static Specifications<Proceso> exists(
            final String comando,
            final String parameter) {

        return Specifications.where(
                Specifications.equals(Proceso_.comando, comando))
                .and(Specifications.equals(Proceso_.parameters, parameter))
                .and(Specifications.not(Specifications.equals(Proceso_.estado, Proceso.State.ACTIVE)));
    }

}
