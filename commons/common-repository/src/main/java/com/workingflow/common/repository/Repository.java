package com.workingflow.common.repository;

import com.google.common.base.Optional;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nullable;
/**
 *
 * Abstracción de acceso a datos. Implementa el patrón 
 * <i>Data Access Object</i>.
 * Se encarga de gestionar el acceso a datos para entidades de tipo {@link T} 
 * con identificador de tipo {@link ID}
 * 
 * @author Cristian Rinaldi <csrinaldi@gmail.com>
 * @author Andrés Testi <andres.a.testi@gmail.com>
 * 
 * @param <T> Tipo de la entidad a persistir
 * @param <ID> Tipo del identificador
 */
public interface Repository<T, ID extends Serializable> extends Serializable {

    /**
     * Cuantificador existencial que determina la existencia de una instancia. 
     * 
     * @param id identificador de la instancia
     * @return true si la entidad existe. false en otro caso.
     */
    boolean exists(ID id);

    @Deprecated
    @Nullable
    T find(ID id);

    /**
     * Busca por id una instancia del tipo {@link T}
     * 
     * @param id identificador de la instancia
     * @return la instancia si es recuperada, u Optional.absent() en otro caso.
     */
    Optional<T> findById(ID id);
    
    /**
     * Guarda una entidad  del tipo {@link T} y devuelve su instancia 
     * actualizada
     * 
     * @param <S> tipo de la entidad
     * @param entity
     * @return la instancia actualizada
     */
    <S extends T> S save(S entity);
    
    Integer save(final Specification<? super T>... specs);

    /**
     * Elimina la entidadd de tipo T.
     * @param entity 
     */
    void delete(T entity);
    
    /**
     * Elimina un conjunto de entidades basado en el predicado generado por la
     * especificacion.
     * 
     * @param specs 
     */
    void deleteBy(Specification<? super T>... specs);

    /**
     * Recupera todas las entidades del tipo {@link T} a partir de un offset 
     * devolviendo una lista de tamaño menor o igual que limit.
     * 
     * @param offset indice a partir del cual recuperar entidades
     * @param limit total de instancias a recuperar
     * @return lista de entidades recuperadas
     */
    List<T> findAll(int offset, int limit);

     /**
     * Recupera todas las entidades del tipo {@link T}
     * 
     * @return lista de entidades recuperadas
     */
    List<T> findAll();

    List<T> findAllById(Iterable<ID> ids);

    @Deprecated
    @Nullable
    T findUnique(Specification<? super T>... specs);

    Optional<T> find(Specification<? super T>... specs);
    
    List<T> findAll(Specification<? super T>... specs);

    List<T> findAll(int offset, int limit, Specification<? super T>... specs);

    @Deprecated
    @Nullable
    T findUnique(Iterable<Specification<? super T>> specs);

    Optional<T> find(Iterable<Specification<? super T>> specs);

    List<T> findAll(Iterable<Specification<? super T>> specs);

    List<T> findAll(int offset, int limit, Iterable<Specification<? super T>> specs);

    long count(Specification<? super T>... specs);
    
    long count();

    Class<T> entityType();
}
