/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.workingflow.akka.commons.persistence;

/**
 *
 * @author csrinaldi
 */
public interface UnitOfWork {
    
    void begin();
    
    void end();
    
    
}
