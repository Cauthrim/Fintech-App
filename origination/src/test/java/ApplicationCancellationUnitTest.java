import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.application.CancellationRequest;
import com.academy.fintech.application.CancellationResponse;
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
@SpringJUnitConfig(classes = { ApplicationCancellationUnitConfiguration.class })
@DisplayName("Application Cancellation Tests")
public class ApplicationCancellationUnitTest {
    @Autowired
    ApplicationServiceGrpc.ApplicationServiceImplBase applicationController;

    @Test
    @DisplayName("Valid Cancellation")
    void testValidCancellation() {
        CancellationRequest request = CancellationRequest.newBuilder()
                .setApplicationId("valid")
                .build();

        StreamRecorder<CancellationResponse> responseObserver = StreamRecorder.create();
        applicationController.cancel(request, responseObserver);
        assertNull(responseObserver.getError());
        List<CancellationResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());

        CancellationResponse response = results.get(0);
        assertTrue(response.getIsCancelled());
    }

    @Test
    @DisplayName("Cancel Non-existent Application")
    void testCancelNonExistent() {
        CancellationRequest request = CancellationRequest.newBuilder()
                .setApplicationId("non-existent")
                .build();

        StreamRecorder<CancellationResponse> responseObserver = StreamRecorder.create();
        applicationController.cancel(request, responseObserver);
        assertNotNull(responseObserver.getError());
        assertEquals(Status.INVALID_ARGUMENT, ((StatusRuntimeException) responseObserver.getError()).getStatus());
    }
}
