package ar.gov.santafe.scit.common.repository.jpa.cdi;

import java.util.Set;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import com.workingflow.common.cdi.CdiUtils;
import com.workingflow.common.repository.jpa.JpaRepositories;
import com.workingflow.common.repository.jpa.JpaRepository;
import com.workingflow.common.repository.jpa.Transactor;


public class RepositoryExtension implements Extension {

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {

        System.out.println("BeforeBeanDiscovery");

    }

    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
        System.out.println("ProcessAnnotatedType");
    }

    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
        System.out.println("AfterBeanDiscovery");
    }

    public <X> void processInjectionTarget(@Observes ProcessInjectionTarget<X> pit,
            BeanManager bm) {

        System.out.println("Procesando punto de inyeccion");

        final InjectionTarget<X> it = pit.getInjectionTarget();
        
        final Provider<EntityManager> em = CdiUtils.providerOf(EntityManager.class, bm);
        final Provider<Transactor> transactor = CdiUtils.providerOf(Transactor.class, bm);
        //use this to read annotations of the class and its members

        final AnnotatedType<X> at = pit.getAnnotatedType();

        if (JpaRepository.class.isAssignableFrom(at.getJavaClass())) {

            InjectionTarget<X> wrapped = new InjectionTarget<X>() {
                @Override
                public void inject(X instance, CreationalContext<X> ctx) {

                    it.inject(instance, ctx);
                    JpaRepositories.initialize((JpaRepository) instance, em, transactor);
                }

                @Override
                public void postConstruct(X instance) {
                    it.postConstruct(instance);
                }

                @Override
                public void preDestroy(X instance) {
                    it.preDestroy(instance);

                }

                @Override
                public void dispose(X instance) {
                    it.dispose(instance);
                }

                @Override
                public Set<InjectionPoint> getInjectionPoints() {
                    return it.getInjectionPoints();
                }

                @Override
                public X produce(CreationalContext<X> ctx) {
                    return it.produce(ctx);
                }
            };
            pit.setInjectionTarget(wrapped);
        }
    }

}
