package com.api.serviceCounter.serviceDesk;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceDeskRepository extends JpaRepository<ServiceDesk, UUID> {

    Optional<ServiceDesk> findByIdAndIsActiveTrue(UUID id);

    Page<ServiceDesk> findAllByIsActiveTrue(Pageable pageable);

    Optional<ServiceDesk> findByNameAndIsActiveTrue(String name);
}