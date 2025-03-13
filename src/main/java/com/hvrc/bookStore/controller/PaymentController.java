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
@RequestMapping("payment")
public class PaymentController {

    private final StripeService stripeService;
    private final OrderService orderService;

    public PaymentController(StripeService stripeService, OrderService orderService) {
        this.stripeService = stripeService;
        this.orderService = orderService;
    }

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
