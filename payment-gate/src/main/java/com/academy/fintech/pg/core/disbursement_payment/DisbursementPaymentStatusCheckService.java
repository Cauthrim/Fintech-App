package com.academy.fintech.pg.core.disbursement_payment;

import com.academy.fintech.pg.configuration.MerchantUrlProperty;
import com.academy.fintech.pg.core.disbursement_payment.db.DisbursementPayment;
import com.academy.fintech.pg.core.disbursement_payment.db.DisbursementPaymentRepository;
import com.academy.fintech.pg.core.disbursement_payment.db.DisbursementPaymentService;
import com.academy.fintech.pg.core.disbursement_payment.db.DisbursementPaymentStatus;
import com.academy.fintech.pg.core.origination.client.ProcessedPaymentClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DisbursementPaymentStatusCheckService {
    private final MerchantUrlProperty merchantUrlProperty;
    private final ProcessedPaymentClientService processedPaymentClientService;
    private final DisbursementPaymentService disbursementPaymentService;
    private final DisbursementPaymentRepository disbursementPaymentRepository;

    @Transactional
    @Scheduled(fixedDelay = 100)
    public void checkPaymentStatus() {
        List<DisbursementPayment> unprocessedPayments =
                disbursementPaymentService.findAllByStatus(DisbursementPaymentStatus.UNPROCESSED);

        for (DisbursementPayment payment : unprocessedPayments) {
            try {
                URL url = new URL(merchantUrlProperty.statusUrl() + payment.getId());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine = in.readLine();
                if (inputLine.equals("true")) {
                    payment.setStatus(DisbursementPaymentStatus.PROCESSED);
                    disbursementPaymentRepository.save(payment);

                    processedPaymentClientService.confirmDisbursement(payment.getId());
                }
                in.close();
            } catch (Exception exception) {
                log.error("Error while checking payment status", exception);
            }
        }
    }
}
