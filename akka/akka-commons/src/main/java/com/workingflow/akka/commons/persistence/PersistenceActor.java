/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.workingflow.akka.commons.persistence;


import com.workingflow.akka.commons.cdi.AwareActor;
import javax.inject.Inject;
import scala.Option;

/**
 *
 * @author csrinaldi
 */
public class PersistenceActor extends AwareActor{
    
    @Inject
    UnitOfWork resources;
    
    @Override
    public void postRestart(Throwable reason) {
        super.postRestart(reason);
        resources.begin();
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        super.preRestart(reason, message); //To change body of generated methods, choose Tools | Templates.
        resources.begin();
    }
    
    @Override
    public void postStop() throws Exception {
        super.postStop();
        resources.end();
    }
    
    @Override
    public void onReceive(Object o) throws Exception {
        
    }
    
}
