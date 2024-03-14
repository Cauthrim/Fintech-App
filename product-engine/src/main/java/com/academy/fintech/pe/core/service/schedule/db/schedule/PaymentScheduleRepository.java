package com.academy.fintech.pe.core.service.schedule.db.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Integer> {
    List<PaymentSchedule> findAllByAgreementId(String agreementId);
}
