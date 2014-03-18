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
package com.workingflow.akka.console;

import akka.actor.ActorSystem;
import com.workingflow.share.protobuff.Protocol;
import com.workingflow.share.protobuff.Protocol.Command;
import com.workingflow.share.protobuff.Protocol.Connect;
import com.workingflow.share.protobuff.Protocol.MetaRequest;
import com.workingflow.share.protobuff.Protocol.Request;
import java.io.Console;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;

/**
 * Documentación de {@link CommandConsole}.
 *
 * @author Cristian Rinaldi <crinaldi@gmail.com>
 * 
 */
@ApplicationScoped
public class CommandConsole {

    private Boolean available = false;
    private final Console console;
    private String user = "guess";
    private final List<String> commands = new ArrayList<String>();
    private boolean loggued = false;
    private UUID clientId;

    private String token = null;

    @Inject
    private ActorSystem system;

    public CommandConsole() {
        this.console = System.console();
        this.available = (this.console != null);
    }

    public void run() {
        console.format(
                "Welcome to Akka Console: \n");
        console.format(
                "Write command (command, help or exit )\n", "");
        String command = "";

        if (available) {
            while (!command.equalsIgnoreCase("exit")) {
                command = console.readLine("%s", "akka:" + user + "->");
                processCommand(command);
            }
        }

        if (command.equalsIgnoreCase("exit")) {
            console.format("akka-" + user + "-> %s\n", "logout");
            System.exit(1);
        }
    }

    public void processCommand(String command) {
        boolean validCommand = true;

        String errorMessage = "";
        MetaRequest.Builder metaReq = MetaRequest.newBuilder();

        Request.Builder reqBuilder = Request.newBuilder();
        reqBuilder.setUuid(UUID.randomUUID().toString());
        reqBuilder.setClientId(clientId.toString());

        String[] args = command.split(" ");
        String toReturn = "";

        if (args[0].equalsIgnoreCase("connect")) {
            Connect.Builder connectBuilder = Connect.newBuilder();
            if (args.length == 3) {
                String[] userPair = args[1].split(":");
                String[] passwdPair = args[2].split(":");

                if (userPair.length == 2 && userPair[0].equals("user")
                        && passwdPair.length == 2 && passwdPair[0].equals("passwd")) {
                    connectBuilder.setUser(userPair[1]);
                    connectBuilder.setPasswd(passwdPair[1]);
                    connectBuilder.setRequest(reqBuilder);
                    metaReq.setConnect(connectBuilder);
                    system.eventStream().publish(metaReq.build());
                } else {
                    toReturn = "Sintax error in user and passwd params: Use: connect user:<user> passwd:<passwd>";
                }
            } else {
                toReturn = "Count parameter error: Use connect user:<user> passwd:<passwd>";
            }

        } else if (args[0].equalsIgnoreCase("help")) {
            if ( args.length > 1 ){
                switch ( args[1] ){
                    case "import" : 
                        toReturn = "Use import factor:[(factors ...)] fork:(fork) type:(type)"
                                + "\n (factors ...) = 2100,0120,0825"
                                + "\n (factors ...) = {P - [0120,0302]}"
                                + "\n type          = parcela|manzana|zona"
                                + "\n fork          = true|false"
                                + "\n priority      = 0|1|2|3|4|5";
                        break;
                    default:
                        toReturn = "Use help <command>";
                        break;
                }
            }else{
                toReturn = "Use help <command>";
            }

        } else if (loggued) {
            if (args[0].equalsIgnoreCase("ls")) {

            } else if (args[0].trim().equalsIgnoreCase("start")) {

            } else if (args[0].equalsIgnoreCase("stop")) {

            } else if (args[0].equalsIgnoreCase("import")) {
                toReturn = "Import Command";
            } else {
                validCommand = false;
                toReturn = "Is not a command WTF?###";
            }

            if (validCommand) {
                Protocol.AuthenticatedRequest.Builder authReqBuild = Protocol.AuthenticatedRequest.newBuilder();
                authReqBuild.setRequest(reqBuilder);
                authReqBuild.setToken(token);
                Command.Builder cmdBuilder = Command.newBuilder();
                cmdBuilder.setCommand(args[0]);
                cmdBuilder.setRequest(authReqBuild);
                //TODO analizar prioridad como parametro del request
                cmdBuilder.setPriority(5);

                if (args.length > 1) {
                    for (int i = 1; i < args.length; i++) {
                        String[] pair = args[i].split(":");
                        if (pair.length == 2) {
                            Protocol.Command.Parameter.Builder param = Protocol.Command.Parameter.newBuilder();
                            param.setName(pair[0]);
                            param.setValue(pair[1]);
                            cmdBuilder.addParams(param);
                        }
                    }

                    commands.add(command);
                }
                metaReq.setCommand(cmdBuilder);
                system.eventStream().publish(metaReq.build());
            } else {
                toReturn = errorMessage;
            }
        } else {
            toReturn = "Not logged: Use connect user:<user> passwd:<passwd>";
        }
        writeResult(toReturn, false);
    }

    public void writeResult(String result, boolean remote) {
        if (remote) {
            System.out.printf("\nakka:remote-%s-> %s\n", user, result);
            System.out.printf("akka:%s->", user);
        } else {
            if (!result.isEmpty()) {
                System.out.printf("akka:%s-> %s\n", user, result);
            } else {
                System.out.printf("akka:%s->\n", user);
            }
        }
        //System.out.printf("\nakka:%s->", user);
        //console.readLine("%s", "akka-" + user + ">");
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setLogged(boolean logged) {
        this.loggued = logged;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}
