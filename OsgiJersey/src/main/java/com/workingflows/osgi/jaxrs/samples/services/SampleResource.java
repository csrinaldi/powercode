/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.workingflows.osgi.jaxrs.samples.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Documentation of {@link SampleResource}.
 * 
 * 
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 */
@Path("sample")
public class SampleResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String sample() {
        return "Working!!!";
    }
    
}
