package com.example.web_petvibe.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
class GetPaymentMethodResponse {
    private String message;
    private boolean success;
    private Long paymentMethodId;
    private String methodName;
    private String description;
    private Boolean isActive;
}


