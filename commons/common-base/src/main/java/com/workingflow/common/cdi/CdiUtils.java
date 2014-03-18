package com.workingflow.common.cdi;

import java.util.Set;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Provider;

/**
 *
 * Utilidades para inspeccionar el contenedor CDI.
 *
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 *
 */
public class CdiUtils {

    private CdiUtils() {
    }

    public static <T> Provider<T> providerOf(final Class<T> type, final BeanManager bm) {
        return new Provider<T>() {
            @Override
            public T get() {
                return findUniqueBean(bm, type);
            }
        };
    }

    public static <T> T findUniqueBean(BeanManager bm, Class<T> type) {

        final Set<Bean<?>> beans = bm.getBeans(type);
        
        if (beans == null || beans.isEmpty()) {
            throw new IllegalStateException("Could not find Class Type Implementation");
        }

        final Bean<?> bean = beans.iterator().next();

        final CreationalContext<?> creationalContext = bm.createCreationalContext(bean);
        return (T) bm.getReference(bean, type, creationalContext);
    }
}
