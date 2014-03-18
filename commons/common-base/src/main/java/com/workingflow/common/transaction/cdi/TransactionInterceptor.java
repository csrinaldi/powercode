package com.workingflow.common.transaction.cdi;

import com.workingflow.common.transaction.Transactional;
import java.lang.reflect.Method;
import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 * 
 */
@Deprecated
//@Transactional
//@Interceptor
public class TransactionInterceptor {
    
    @Transactional
    private static class Internal {
    }
    
    @Resource
    private UserTransaction utx;
   
    // TODO(dhanji): Cache this method's results.
    private Transactional readTransactionMetadata(InvocationContext methodInvocation) {
        Transactional transactional;
        Method method = methodInvocation.getMethod();
        Class<?> targetClass = methodInvocation.getTarget().getClass();
        
        transactional = method.getAnnotation(Transactional.class);
        if (null == transactional) {
            // If none on method, try the class.
            transactional = targetClass.getAnnotation(Transactional.class);
        }
        if (null == transactional) {
            // If there is no transactional annotation present, use the default
            transactional = Internal.class.getAnnotation(Transactional.class);
        }
        
        return transactional;
    }
    
    @AroundInvoke
    public Object invoke(InvocationContext ic) throws Throwable {
        
        System.out.println("capturando un metodo transaccional");
        
        Transactional transactional = readTransactionMetadata(ic);

        // Allow 'joining' of transactions if there is an enclosing @Transactional method.
        if (utx.getStatus() == Status.STATUS_ACTIVE) {
            return ic.proceed();
        }
        
        utx.begin();
        
        Object result;
        try {
            result = ic.proceed();
            
        } catch (Exception e) {
            //commit transaction only if rollback didnt occur
            if (rollbackIfNecessary(transactional, e)) {
                utx.commit();
            }

            //propagate whatever exception is thrown anyway
            throw e;
        }
        //everything was normal so commit the txn (do not move into try block above as it
        //  interferes with the advised method's throwing semantics)

        utx.commit();

        //or return result
        return result;
    }

    /**
     * Returns True if rollback DID NOT HAPPEN (i.e. if commit should continue).
     *
     * @param transactional The metadata annotaiton of the method
     * @param e The exception to test for rollback
     * @param txn A JPA Transaction to issue rollbacks on
     */
    private boolean rollbackIfNecessary(Transactional transactional, Exception e) throws IllegalStateException, SecurityException, SystemException {
        boolean commit = true;

        //check rollback clauses
        for (Class<? extends Exception> rollBackOn : transactional.rollbackOn()) {

            //if one matched, try to perform a rollback
            if (rollBackOn.isInstance(e)) {
                commit = false;

                //check ignore clauses (supercedes rollback clause)
                for (Class<? extends Exception> exceptOn : transactional.ignore()) {
                    //An exception to the rollback clause was found, DON'T rollback
                    // (i.e. commit and throw anyway)
                    if (exceptOn.isInstance(e)) {
                        commit = true;
                        break;
                    }
                }

                //rollback only if nothing matched the ignore check
                if (!commit) {
                    utx.rollback();
                }
                //otherwise continue to commit

                break;
            }
        }
        
        return commit;
    }
}
