package com.hvrc.bookStore.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {

    private Long id;
    private Long customerId;
    private String paymentId;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private String status;
}
