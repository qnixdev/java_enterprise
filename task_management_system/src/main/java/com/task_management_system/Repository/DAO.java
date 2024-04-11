package com.task_management_system.Repository;

import java.util.Optional;
import java.util.UUID;

public interface DAO<T> {
    Optional<T> find(UUID id);

    Iterable<T> findAll();
}