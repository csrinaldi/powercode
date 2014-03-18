package com.workingflow.common.transaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 *
 * Denota que un método requiere abrir una transacción de JTA para ejecutarse.
 * 
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 * 
 */
@Inherited
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {

    @Nonbinding
    Class<? extends Exception>[] rollbackOn() default RuntimeException.class;

    @Nonbinding
    Class<? extends Exception>[] ignore() default {};
}