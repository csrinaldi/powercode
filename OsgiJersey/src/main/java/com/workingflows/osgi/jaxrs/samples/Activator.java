package com.workingflows.osgi.jaxrs.samples;

import com.workingflows.osgi.jaxrs.samples.conf.MyJaxApp;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.servlet.http.HttpServlet;
import org.glassfish.jersey.servlet.ServletContainer;
import org.ops4j.pax.web.service.WebContainer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;

public class Activator implements BundleActivator {

    private final ServletContainer container;
    private ServiceReference webContainerRef;

    public Activator() {
        container = new ServletContainer(new MyJaxApp());
        
                
    }
    
    public void start(BundleContext bc) throws Exception {
        /**
         * The pax-web-war service can take a little longer to start, we can't
         * say how much it will take, so we do need to sit down a while and wait
         * it's availability in order to use it's reference.
         *
         * This is a MUST, it's really important - mostly when using the
         * config.ini file (Equinox).
         *
         * Anaximandro April 19, 2010.
         */
        int counter = 0;
        boolean started = false;
        while (!started) {

            webContainerRef = bc.getServiceReference(WebContainer.class);
            started = webContainerRef != null;
            if (started) {
                final WebContainer webContainer = (WebContainer) bc
                        .getService(webContainerRef);
                if (webContainer != null) {
                    // create a default context to share between registrations
                    final HttpContext httpContext = webContainer
                            .createDefaultHttpContext();
                    // set a session timeout of 10 minutes
                    webContainer.setSessionTimeout(10, httpContext);
                    // register the hello world servlet for filtering with url
                    // pattern
                    final Dictionary<String, Object> initParamsServlet = new Hashtable<String, Object>();
                    //initParamsServlet.put("javax.ws.rs.Application", "com.workingflows.osgi.jaxrs.samples.conf.MyJaxApp");
                    webContainer.registerServlet((HttpServlet) container, // registered
                            new String[]{"/rest/*"}, // url patterns
                            initParamsServlet, // init params
                            httpContext // http context
                    );

                    webContainer.registerResources("/html", "/html",
                            httpContext);

                    // register a welcome file
                    webContainer.registerWelcomeFiles(
                            new String[]{"index.html"}, false, httpContext);
                }
            } else {
                // wait, throw exception after 5 retries.
                if (counter > 10) {
                    throw new Exception(
                            "Could not start the helloworld-wc service, WebContainer service not started or not available.");
                } else {
                    counter++;
                    Thread.sleep(counter * 1000);
                }
            }
        }
    }

    public void stop(BundleContext bc) throws Exception {
        if (webContainerRef != null) {
            bc.ungetService(webContainerRef);
            webContainerRef = null;
        }
    }

}
