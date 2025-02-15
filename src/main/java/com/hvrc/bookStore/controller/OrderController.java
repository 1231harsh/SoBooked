package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.dto.CreatePaymentResponse;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.service.OrderService;
import com.hvrc.bookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<CreatePaymentResponse> placeOrder(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Long cartId = user.getCart().getId();
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
