import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.application.CancellationRequest;
import com.academy.fintech.application.CancellationResponse;
import com.academy.fintech.origination.Application;
import com.academy.fintech.origination.core.client.db.LoanClientService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DisplayName("Integration Tests")
public class IntegrationTest {
    private static final Random rand = new Random();
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.1-alpine");

    @Autowired
    ApplicationServiceGrpc.ApplicationServiceImplBase applicationController;

    @Autowired
    LoanClientService loanClientService;

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
    @DisplayName("Valid Application Creation")
    void testApplicationCreation() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setEmail("example" + rand.nextInt(Integer.MAX_VALUE) + "@mail.com")
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
        assertNotNull(response);
    }

    @Test
    @DisplayName("Duplicate Application Creation")
    void testDuplicateApplicationCreation() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setEmail("example" + rand.nextInt(Integer.MAX_VALUE) + "@mail.com")
                .setFirstName("Danila")
                .setLastName("Kurkov")
                .setSalary(100000)
                .setDisbursementAmount(500000)
                .build();

        StreamRecorder<ApplicationResponse> responseObserver = StreamRecorder.create();
        applicationController.create(request, responseObserver);
        applicationController.create(request, responseObserver);
        assertNotNull(responseObserver.getError());
        StatusRuntimeException exception = (StatusRuntimeException) responseObserver.getError();
        assertEquals(Status.ALREADY_EXISTS, exception.getStatus());
    }

    @Test
    @DisplayName("Invalid Email")
    void testInvalidEmail() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setEmail("exa|mple''@mai'l.c'|om")
                .setFirstName("Danila")
                .setLastName("Kurkov")
                .setSalary(100000)
                .setDisbursementAmount(500000)
                .build();

        StreamRecorder<ApplicationResponse> responseObserver = StreamRecorder.create();
        applicationController.create(request, responseObserver);
        assertNotNull(responseObserver.getError());
        StatusRuntimeException exception = (StatusRuntimeException) responseObserver.getError();
        assertEquals(Status.INVALID_ARGUMENT, exception.getStatus());
    }

    @Test
    @DisplayName("Valid Cancel Application")
    void testValidCancelApplication() {
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setEmail("example" + rand.nextInt(Integer.MAX_VALUE) + "@mail.com")
                .setFirstName("Danila")
                .setLastName("Kurkov")
                .setSalary(100000)
                .setDisbursementAmount(500000)
                .build();

        StreamRecorder<ApplicationResponse> creationObserver = StreamRecorder.create();
        applicationController.create(request, creationObserver);
        assertNull(creationObserver.getError());
        List<ApplicationResponse> results = creationObserver.getValues();
        assertEquals(1, results.size());

        ApplicationResponse response = results.get(0);
        StreamRecorder<CancellationResponse> cancellationObserver = StreamRecorder.create();
        applicationController.cancel(CancellationRequest.newBuilder()
                .setApplicationId(response.getApplicationId())
                .build(), cancellationObserver);
        assertNull(cancellationObserver.getError());
    }

    @Test
    @DisplayName("Invalid Cancel Application")
    void testInvalidCancel() {
        StreamRecorder<CancellationResponse> cancellationObserver = StreamRecorder.create();
        applicationController.cancel(CancellationRequest.newBuilder()
                .setApplicationId("")
                .build(), cancellationObserver);
        assertNotNull(cancellationObserver.getError());
    }
}
