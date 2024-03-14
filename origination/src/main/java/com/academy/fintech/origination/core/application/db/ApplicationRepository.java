package com.academy.fintech.origination.core.application.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    Application findByClientIdAndStatus(String clientId, ApplicationStatus status);

    List<Application> findAllByStatus(ApplicationStatus status);
}
