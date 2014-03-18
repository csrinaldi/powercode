package com.workingflow.common.repository.jpa;

import com.google.common.base.Preconditions;


import javax.inject.Provider;
import javax.persistence.EntityManager;

public class JpaRepositories {

    public static void initialize(JpaRepository<?, ?> repository, Provider<EntityManager> em,
            Provider<Transactor> transactor) {
        Preconditions.checkState(repository.em == null, "EntityManager already initialized");
        Preconditions.checkState(repository.transactor == null, "Transactor already initialized");
        repository.em = em;
        repository.transactor = transactor;
    }
}
