package com.workingflow.common.repository;

import com.google.common.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utilidades para repositorios
 *
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 */
public class Repositories {

    private Repositories() {
    }

    /**
     * Devuelve el tipo de entidad a la cual corresponde el repositorio
     * 
     * @param repositoryType tipo del repositorio
     * @return tipo de la entidad
     */
    public static <T> Class<T> entityType(Class<? extends Repository<T, ?>> repositoryType) {
        @SuppressWarnings("unchecked")
        final TypeToken<Repository<T, ?>> token =
                (TypeToken<Repository<T, ?>>) TypeToken.of(repositoryType).getSupertype(Repository.class);
        final ParameterizedType paramType = (ParameterizedType) token.getType();
        @SuppressWarnings("unchecked")
        final Type entityType = paramType.getActualTypeArguments()[0];
        return (Class<T>) TypeToken.of(entityType).getRawType();
    }
}
