package com.hvrc.bookStore.service;

import com.hvrc.bookStore.dto.CreatePaymentResponse;
import com.hvrc.bookStore.entity.*;
import com.hvrc.bookStore.repository.OrderRepository;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private CartService cartService;


    @Mock
    private StripeService stripeService;

    @InjectMocks
    private OrderService orderService;

    private Cart cart;
    private User user;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setBuyPrice(500.0);
        book.setRentalPrice(50.0);

        CartItems cartItem = new CartItems();
        cartItem.setBook(book);
        cartItem.setRenting(false);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setCartItems(List.of(cartItem));

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setTotalAmount(500.0);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
    }

    @Test
    void testPlaceOrder_Success() throws Exception {
        PaymentIntent mockPaymentIntent = mock(PaymentIntent.class);
        when(mockPaymentIntent.getId()).thenReturn("pi_12345");
        when(mockPaymentIntent.getClientSecret()).thenReturn("secret_abc");

        when(cartService.getCartById(1L)).thenReturn(cart);
        when(stripeService.createPaymentIntent(anyDouble(), anyString())).thenReturn(mockPaymentIntent);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreatePaymentResponse response = orderService.placeOrder(1L);

        assertNotNull(response);
        assertEquals("secret_abc", response.getClientSecret());
        verify(orderRepository, times(2)).save(any(Order.class));
        verify(orderItemService, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testPlaceOrder_PaymentFails() throws Exception {
        when(cartService.getCartById(1L)).thenReturn(cart);
        when(stripeService.createPaymentIntent(anyDouble(), anyString())).thenThrow(new RuntimeException("Payment error"));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.placeOrder(1L));
        assertEquals("Payment failed", exception.getMessage());
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void testHandlePayment_Success() throws Exception {
        order.setPaymentId("pi_12345");
        order.setStatus("PENDING");

        when(orderRepository.findByPaymentId("pi_12345")).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(stripeService).confirmPayment("pi_12345");
        doNothing().when(cartService).clearCartByUserId(user.getId());
        doNothing().when(cartService).deleteCartByUserId(user.getId());

        assertDoesNotThrow(() -> orderService.handlePayment("pi_12345"));
        assertEquals("PAID", order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testHandlePayment_OrderNotFound() {
        when(orderRepository.findByPaymentId("pi_12345")).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.handlePayment("pi_12345"));
        assertEquals("Order not found for payment ID: pi_12345", exception.getMessage());
    }
}