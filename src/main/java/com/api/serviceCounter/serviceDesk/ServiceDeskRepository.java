package com.api.serviceCounter.serviceDesk;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceDeskRepository extends JpaRepository<ServiceDesk, UUID> {
    Optional<ServiceDesk> findByName(String name);
}