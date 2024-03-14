package merchant.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DisbursementRequest (
    String email,
    @JsonProperty("disbursement_amount")
    String disbursementAmount
) {}
