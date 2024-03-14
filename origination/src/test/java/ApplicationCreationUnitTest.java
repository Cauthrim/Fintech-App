import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringJUnitConfig(classes = { ApplicationCreationUnitConfiguration.class })
@DisplayName("Application Creation Tests")
public class ApplicationCreationUnitTest {
    @Autowired
    ApplicationServiceGrpc.ApplicationServiceImplBase applicationController;

    @Test
    @DisplayName("Valid Creation")
    void testCreation() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setEmail("example@mail.com")
                .setFirstName("Danila")
                .setLastName("Kurkov")
                .setSalary(100000)
                .setDisbursementAmount(500000)
                .build();

        StreamRecorder<ApplicationResponse> responseObserver = StreamRecorder.create();
        applicationController.create(request, responseObserver);
        assertNull(responseObserver.getError());
        List<ApplicationResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());

        ApplicationResponse response = results.get(0);
        assertEquals("ok", response.getApplicationId());
    }

    @Test
    @DisplayName("Invalid Email")
    void testInvalidEmail() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setEmail("example|@mail.com")
                .setFirstName("Danila")
                .setLastName("Kurkov")
                .setSalary(100000)
                .setDisbursementAmount(500000)
                .build();

        StreamRecorder<ApplicationResponse> responseObserver = StreamRecorder.create();
        applicationController.create(request, responseObserver);
        assertNotNull(responseObserver.getError());
        assertEquals(Status.INVALID_ARGUMENT, ((StatusRuntimeException) responseObserver.getError()).getStatus());
    }

    @Test
    @DisplayName("Duplicate Application")
    void testDuplicateApplication() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setEmail("duplicate@mail.com")
                .setFirstName("Danila")
                .setLastName("Kurkov")
                .setSalary(100000)
                .setDisbursementAmount(500000)
                .build();

        StreamRecorder<ApplicationResponse> responseObserver = StreamRecorder.create();
        applicationController.create(request, responseObserver);
        assertNotNull(responseObserver.getError());
        assertEquals(Status.ALREADY_EXISTS, ((StatusRuntimeException) responseObserver.getError()).getStatus());
    }
}
