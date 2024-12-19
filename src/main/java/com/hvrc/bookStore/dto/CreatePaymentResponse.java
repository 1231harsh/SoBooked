package com.hvrc.bookStore.dto;

import lombok.Data;

@Data
public class CreatePaymentResponse {

    private String clientSecret;
    private Long orderId;

    public CreatePaymentResponse(Long orderId, String clientSecret) {
        this.orderId = orderId;
        this.clientSecret = clientSecret;
    }
    public CreatePaymentResponse(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
