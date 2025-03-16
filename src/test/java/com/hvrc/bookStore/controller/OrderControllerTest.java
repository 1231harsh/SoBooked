package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.dto.CreatePaymentResponse;
import com.hvrc.bookStore.entity.Cart;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.service.OrderService;
import com.hvrc.bookStore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Mock
    private Principal principal;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrder_Success() {
        User mockUser = new User();
        Cart mockCart = new Cart();
        mockCart.setId(1L);
        mockUser.setCart(mockCart);

        when(principal.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(mockUser);

        CreatePaymentResponse mockResponse = new CreatePaymentResponse((long) 123, "url");
        when(orderService.placeOrder(1L)).thenReturn(mockResponse);

        ResponseEntity<CreatePaymentResponse> response = orderController.placeOrder(principal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(123, response.getBody().getOrderId());
    }

    @Test
    void testConfirmPayment_Success() {
        doNothing().when(orderService).handlePayment("payment123");

        ResponseEntity<String> response = orderController.confirmPayment("payment123");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Payment confirmed", response.getBody());
    }

    @Test
    void testConfirmPayment_Failure() {
        doThrow(new RuntimeException("Payment failed"))
                .when(orderService).handlePayment("payment123");

        ResponseEntity<String> response = orderController.confirmPayment("payment123");


        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Payment failed", response.getBody());
    }
}
