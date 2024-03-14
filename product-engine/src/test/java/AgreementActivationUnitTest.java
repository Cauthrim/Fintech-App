import com.academy.fintech.agreement.AgreementActivation;
import com.academy.fintech.agreement.AgreementServiceGrpc;
import com.academy.fintech.agreement.ProtoPaymentSchedule;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringJUnitConfig(classes = { AgreementActivationUnitConfiguration.class })
@DisplayName("Agreement Activation Tests")
public class AgreementActivationUnitTest {
    @Autowired
    private AgreementServiceGrpc.AgreementServiceImplBase agreementController;

    @Test
    @DisplayName("Valid Activation")
    void testValidActivation() {
        AgreementActivation request = AgreementActivation.newBuilder()
                .setAgreementId("mock")
                .setDate("2023-11-14")
                .build();

        StreamRecorder<ProtoPaymentSchedule> responseObserver = StreamRecorder.create();

        // repository returns null on save without db
        assertThrows(NullPointerException.class,
                () -> agreementController.activateAgreement(request, responseObserver));
    }

    @Test
    @DisplayName("Invalid  AgreementId")
    void testInvalidAgreementId() {
        AgreementActivation request = AgreementActivation.newBuilder()
                .setAgreementId("mocka")
                .setDate("2023-11-14")
                .build();

        StreamRecorder<ProtoPaymentSchedule> responseObserver = StreamRecorder.create();
        agreementController.activateAgreement(request, responseObserver);
        assertNull(responseObserver.getError());
        List<ProtoPaymentSchedule> results = responseObserver.getValues();
        assertEquals(1, results.size());

        ProtoPaymentSchedule response = results.get(0);
        assertEquals(ProtoPaymentSchedule.newBuilder()
                .setAgreementId("mocka")
                .setId(-1)
                .setVersion(1)
                .build(), response);
    }
}
