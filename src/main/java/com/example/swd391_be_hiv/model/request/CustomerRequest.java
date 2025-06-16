package com.example.swd391_be_hiv.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotNull(message = "Account ID is required")
    private Long accountId;


}
