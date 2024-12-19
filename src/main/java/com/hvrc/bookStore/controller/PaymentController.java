package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.service.OrderService;
import com.hvrc.bookStore.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private OrderService orderService;


    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(@RequestParam String paymentId) throws Exception {
        try{
            stripeService.confirmPayment(paymentId);
            orderService.handlePayment(paymentId);
            return ResponseEntity.ok("Payment confirmed");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
