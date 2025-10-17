package com.example.web_petvibe.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequest {

    @NotBlank(message = "Method name is required")
    private String methodName;

    private String description;

    private Boolean isActive;
}