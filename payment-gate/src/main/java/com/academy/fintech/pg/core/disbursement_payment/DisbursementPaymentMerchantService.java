package com.academy.fintech.pg.core.disbursement_payment;

import com.academy.fintech.pg.configuration.MerchantUrlProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class DisbursementPaymentMerchantService {
    private final MerchantUrlProperty merchantUrlProperty;

    public String requestPayment(String email, int disbursementAmount) {
        String requestBody = String.format("{\"email\":\"%s\", \"disbursement_amount\":\"%s\"}",
                email, disbursementAmount);
        try {
            URL url = new URL(merchantUrlProperty.requestUrl());
            BufferedReader in = getBufferedReader(url, requestBody);
            return in.readLine();
        } catch (Exception exception) {
            log.error("Error while requesting payment", exception);
        }
        return "";
    }

    private BufferedReader getBufferedReader(URL url, String requestBody) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(requestBody.getBytes().length));
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(requestBody);
        wr.close();
        return new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }
}
