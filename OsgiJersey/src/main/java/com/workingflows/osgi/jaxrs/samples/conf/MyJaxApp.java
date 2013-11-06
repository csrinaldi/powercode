package com.workingflows.osgi.jaxrs.samples.conf;

import com.workingflows.osgi.jaxrs.samples.services.SampleResource;
import com.workingflows.osgi.jaxrs.samples.services.SseResource;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.sse.SseFeature;

/**
 * JAX-RS Application
 *
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 */
//@ApplicationPath("resources")
//@PathPrefix
public class MyJaxApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(SseFeature.class);
        s.add(SampleResource.class);
        s.add(SseResource.class);
        return s;
    }

}
