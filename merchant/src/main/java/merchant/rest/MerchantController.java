package merchant.rest;

import merchant.rest.dto.DisbursementRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/merchant/disbursement")
public class MerchantController {
    @PostMapping("/requests")
    public String receivePaymentRequest(@RequestBody DisbursementRequest disbursementRequest) {
        return String.valueOf(new Random().nextInt(1000));
    }

    @GetMapping("/status/{id}")
    public boolean checkPaymentStatus() {
        return new Random().nextBoolean();
    }
}
