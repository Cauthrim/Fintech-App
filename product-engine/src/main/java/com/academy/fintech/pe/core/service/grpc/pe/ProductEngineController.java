package com.academy.fintech.pe.core.service.grpc.pe;

import com.academy.fintech.pe.ClientId;
import com.academy.fintech.pe.LoanParameters;
import com.academy.fintech.pe.OverduePaymentDates;
import com.academy.fintech.pe.PeriodPayment;
import com.academy.fintech.pe.ProductEngineServiceGrpc;
import com.academy.fintech.pe.core.service.schedule.SchedulePaymentAccessService;
import com.academy.fintech.pe.core.service.util.BigDecimalUtils;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.math.BigDecimal;
import java.util.List;

@GRpcService
@Slf4j
@RequiredArgsConstructor
public class ProductEngineController extends ProductEngineServiceGrpc.ProductEngineServiceImplBase {
    private final SchedulePaymentAccessService schedulePaymentAccessService;

    @Override
    public void getPeriodPayment(LoanParameters request, StreamObserver<PeriodPayment> observer) {
        log.info("Got request: {}", request);

        BigDecimal periodPayment = BigDecimalUtils.calculatePaymentSchedule(new BigDecimal(request.getInterest()),
                new BigDecimal(request.getDisbursementAmount()),
                new BigDecimal(request.getOriginationAmount()),
                request.getTerm())
                // the method called calculates the whole schedule, but we need a singular period payment.
                .get(0).getPeriodPayment();
        PeriodPayment response = PeriodPayment.newBuilder().setPayment(periodPayment.toString()).build();
        observer.onNext(response);
        observer.onCompleted();
    }

    @Override
    public void getOverduePaymentDates(ClientId request, StreamObserver<OverduePaymentDates> observer) {
        log.info("Got request: {}", request);

        List<String> overduePaymentDates = schedulePaymentAccessService.getOverduePaymentDates(request.getClientId());
        OverduePaymentDates response = OverduePaymentDates.newBuilder().addAllDueDate(overduePaymentDates).build();
        observer.onNext(response);
        observer.onCompleted();
    }
}
