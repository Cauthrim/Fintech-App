import com.academy.fintech.agreement.*;
import com.academy.fintech.pe.Application;
import com.academy.fintech.pe.core.service.agreement.db.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.AgreementRepository;
import com.academy.fintech.pe.core.service.agreement.db.AgreementStatus;
import com.academy.fintech.pe.core.service.product.db.ProductRepository;
import com.academy.fintech.pe.core.service.schedule.db.payment.SchedulePaymentRepository;
import com.academy.fintech.pe.core.service.schedule.db.schedule.PaymentScheduleRepository;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DisplayName("Integration Tests")
public class IntegrationTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.1-alpine");

    @Autowired
    AgreementRepository agreementRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    SchedulePaymentRepository schedulePaymentRepository;

    @Autowired
    AgreementServiceGrpc.AgreementServiceImplBase agreementController;

    @DynamicPropertySource
    static void setPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    @DisplayName("DB Save Test")
    void testSaveAgreement() {
        Agreement saveAgreement = new Agreement();
        saveAgreement.setProductCode("Cl1.0");
        saveAgreement.setStatus(AgreementStatus.NEW);
        saveAgreement.setTerm(12);
        saveAgreement.setOriginationAmount(new BigDecimal(5000));
        saveAgreement.setInterest(new BigDecimal(8));
        saveAgreement.setDisbursementAmount(new BigDecimal(495000));
        saveAgreement.setClientId("client");

        Agreement agreement = agreementRepository.save(saveAgreement);
        Agreement foundAgreement = agreementRepository.findById(agreement.getId()).orElse(null);
        assertNotNull(foundAgreement);
        assertEquals("Cl1.0", foundAgreement.getProductCode());
    }

    @Test
    @DisplayName("Liquibase Test")
    void testLiquibase() {
        assertNotNull(productRepository.findByCode("Cl1.0"));
    }

    @Test
    @DisplayName("Integrated Agreement Creation")
    void testAgreementCreation() {
        ProtoAgreement request = ProtoAgreement.newBuilder()
                .setProductCode("Cl1.0")
                .setClientId("client")
                .setInterest("8")
                .setTerm(12)
                .setDisbursementAmount(String.valueOf(495000))
                .setOriginationAmount(String.valueOf(5000))
                .build();

        StreamRecorder<ProtoAgreementId> responseObserver = StreamRecorder.create();
        agreementController.createAgreement(request, responseObserver);
        assertNull(responseObserver.getError());
        List<ProtoAgreementId> results = responseObserver.getValues();
        assertEquals(1, results.size());

        ProtoAgreementId response = results.get(0);
        assertNotEquals(ProtoAgreementId.newBuilder().setId("").build(), response);
    }

    @Test
    @DisplayName("Integrated Agreement Activation")
    void testAgreementActivation() {
        ProtoAgreement request = ProtoAgreement.newBuilder()
                .setProductCode("Cl1.0")
                .setClientId("client")
                .setInterest("8")
                .setTerm(12)
                .setDisbursementAmount(String.valueOf(495000))
                .setOriginationAmount(String.valueOf(5000))
                .build();

        StreamRecorder<ProtoAgreementId> responseObserver = StreamRecorder.create();
        agreementController.createAgreement(request, responseObserver);
        assertNull(responseObserver.getError());
        List<ProtoAgreementId> results = responseObserver.getValues();
        assertEquals(1, results.size());

        ProtoAgreementId response = results.get(0);
        assertNotEquals(ProtoAgreementId.newBuilder().setId("").build(), response);

        AgreementActivation activationRequest = AgreementActivation.newBuilder()
                .setAgreementId(response.getId())
                .setDate("2023-11-14")
                .build();
        StreamRecorder<ProtoPaymentSchedule> activationResponseObserver = StreamRecorder.create();
        agreementController.activateAgreement(activationRequest, activationResponseObserver);
        assertNull(activationResponseObserver.getError());
        List<ProtoPaymentSchedule> activationResults = activationResponseObserver.getValues();
        assertEquals(1, activationResults.size());

        ProtoPaymentSchedule activationResponse = activationResults.get(0);
        assertEquals(response.getId(), activationResponse.getAgreementId());

        Agreement agreement = agreementRepository.findById(response.getId()).orElse(null);
        assertNotNull(agreement);
        assertEquals(agreement.getStatus(), AgreementStatus.ACTIVE);
        assertEquals(agreement.getDisbursementDate().toString(), "2023-11-14");
    }
}
