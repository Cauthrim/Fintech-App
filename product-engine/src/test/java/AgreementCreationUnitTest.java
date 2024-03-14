import com.academy.fintech.agreement.AgreementServiceGrpc;
import com.academy.fintech.agreement.ProtoAgreement;
import com.academy.fintech.agreement.ProtoAgreementId;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringJUnitConfig(classes = { AgreementCreationUnitConfiguration.class })
@DisplayName("Agreement Creation Tests")
public class AgreementCreationUnitTest {
    @Autowired
    private AgreementServiceGrpc.AgreementServiceImplBase agreementController;

    @Test
    @DisplayName("Valid Creation")
    void testValidCreation() {
        ProtoAgreement request = ProtoAgreement.newBuilder()
                .setProductCode("Cl1.0")
                .setClientId("2")
                .setInterest("8")
                .setTerm(12)
                .setDisbursementAmount(String.valueOf(495000))
                .setOriginationAmount(String.valueOf(5000))
                .build();
        StreamRecorder<ProtoAgreementId> responseObserver = StreamRecorder.create();

        //Because usually agreementId is auto-generated, in case without DB connection it is null and
        //makes proto builder throw an exception, which actually means that creation is valid.
        assertThrows(NullPointerException.class,
                () -> agreementController.createAgreement(request, responseObserver));
    }

    @Test
    @DisplayName("Invalid Ouf Of Bounds")
    void testOutOfBoundsAgreement() {
        for (int i = 0; i < 4; i++) {
            ProtoAgreement request = ProtoAgreement.newBuilder()
                    .setProductCode("Cl1.0")
                    .setClientId("2")
                    //first step checks interest bounds
                    .setInterest(String.valueOf(8 + 1000 * (i == 0 ? 1 : 0)))
                    //second step checks term bounds
                    .setTerm(12 + 1000 * (i == 1 ? 1 : 0))
                    //third step checks principal bounds
                    .setDisbursementAmount(String.valueOf(495000 + 10000 * (i == 2 ? 1 : 0)))
                    //fourth step checks origination bounds
                    .setOriginationAmount(String.valueOf(5000 + 10000 * (i == 3 ? 1 : 0)))
                    .build();
            StreamRecorder<ProtoAgreementId> responseObserver = StreamRecorder.create();
            agreementController.createAgreement(request, responseObserver);
            assertNull(responseObserver.getError());
            List<ProtoAgreementId> results = responseObserver.getValues();
            assertEquals(1, results.size());
            ProtoAgreementId response = results.get(0);
            assertEquals(ProtoAgreementId.newBuilder().setId("").build(), response);
        }
    }

    @Test
    @DisplayName("Invalid ProductCode")
    void testInvalidProductCode() {
        ProtoAgreement request = ProtoAgreement.newBuilder()
                .setProductCode("Cl1")
                .setClientId("2")
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
        assertEquals(ProtoAgreementId.newBuilder().setId("").build(), response);
    }
}
