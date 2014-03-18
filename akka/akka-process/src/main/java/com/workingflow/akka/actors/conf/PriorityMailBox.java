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
package com.workingflow.akka.actors.conf;

import akka.actor.ActorSystem;
import akka.dispatch.Envelope;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedPriorityMailbox;
import com.typesafe.config.Config;
import com.workingflow.share.protobuff.Protocol;
import java.util.Comparator;
import java.util.List;

/**
 * Documentación de {@link PriorityMailBox}.
 *
 * @author Cristian Rinaldi <a href="mailto:csrinaldi@gmail.com?Subject=WorkinFlow-Akka">csrinaldi@gmail.com</a>
 *
 * Workingflow
 */
public class PriorityMailBox extends UnboundedPriorityMailbox {
    
    private static int LOWER_PRIORITY = 5;

    public PriorityMailBox(Comparator<Envelope> cmp, int initialCapacity) {
        super(cmp, initialCapacity);
    }

    public PriorityMailBox(ActorSystem.Settings settings, Config config) {
        super(new PriorityGenerator() {
            @Override
            public int gen(Object message) {
                if (message instanceof Protocol.Command) {
                    Protocol.Command req = (Protocol.Command) message;
                    return findPriority(req);
                }else{
                    return LOWER_PRIORITY;
                }

            }

            private int findPriority(Protocol.Command req) {
                List<Protocol.Command.Parameter> params = req.getParamsList();
                for (Protocol.Command.Parameter parameter : params) {
                    if ( parameter.getName().equals("priority") ){
                        return Integer.valueOf(parameter.getValue()).intValue();
                    }
                }
                return LOWER_PRIORITY;
            }
        });
    }

}
