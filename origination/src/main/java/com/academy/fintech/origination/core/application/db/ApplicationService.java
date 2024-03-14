package com.academy.fintech.origination.core.application.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public Application findByClientIdAndStatus(String clientId, ApplicationStatus status) {
        return applicationRepository.findByClientIdAndStatus(clientId, status);
    }

    public List<Application> findAllNewApplications() {
        return applicationRepository.findAllByStatus(ApplicationStatus.NEW);
    }

    public Application findById(String applicationId) {
        return applicationRepository.findById(applicationId).orElse(null);
    }

    public void delete(Application application) {
        applicationRepository.delete(application);
    }

    public Application save(Application application) {
        return applicationRepository.save(application);
    }
}
