package com.workingflows.osgi.jaxrs.samples.conf;

import com.workingflows.osgi.jaxrs.samples.services.SampleResource;
import com.workingflows.osgi.jaxrs.samples.services.SseResource;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * JAX-RS Application
 *
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 */
@ApplicationPath("resources")
public class MyJaxApp extends ResourceConfig {

    public MyJaxApp() {
        register(SseFeature.class);
        register(SampleResource.class);
        register(SseResource.class);
        //packages("com.workingflows.osgi.jaxrs.samples.services");
    }
}
