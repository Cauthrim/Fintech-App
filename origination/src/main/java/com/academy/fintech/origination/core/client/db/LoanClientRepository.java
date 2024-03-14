package com.academy.fintech.origination.core.client.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanClientRepository extends JpaRepository<LoanClient, String> {
    Optional<LoanClient> findByEmail(String email);
}
