package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.dto.CreatePaymentResponse;
import com.hvrc.bookStore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<CreatePaymentResponse> placeOrder(@RequestParam("cartId") Long cartId) {
        CreatePaymentResponse createPaymentResponse = orderService.placeOrder(cartId);
        return ResponseEntity.ok(createPaymentResponse);
    }

    @PostMapping("/pay")
    public ResponseEntity<String> confirmPayment(@RequestParam("paymentId") String paymentId) {
        try{
            orderService.handlePayment(paymentId);
            return ResponseEntity.ok("Payment confirmed");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
