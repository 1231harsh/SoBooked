package com.hvrc.bookStore.service;

import com.hvrc.bookStore.dto.CreatePaymentResponse;
import com.hvrc.bookStore.dto.OrderDTO;
import com.hvrc.bookStore.dto.OrderMapper;
import com.hvrc.bookStore.entity.*;
import com.hvrc.bookStore.repository.OrderRepository;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final CartService cartService;
    private final BookService bookService;
    private final StripeService stripeService;

    public OrderService(OrderRepository orderRepository, OrderItemService orderItemService, CartService cartService, BookService bookService, StripeService stripeService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.cartService = cartService;
        this.bookService = bookService;
        this.stripeService = stripeService;
    }

    @Transactional
    public CreatePaymentResponse placeOrder(Long cartId) {
        Cart cart = cartService.getCartById(cartId);

        List<CartItems> cartItems = cart.getCartItems();

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.isRenting() ? item.getBook().getRentalPrice() : item.getBook().getBuyPrice())
                .sum();

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTotalAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        orderRepository.save(order);

        for (CartItems cartItem : cartItems) {
            Book book = cartItem.getBook();

            boolean isRenting = cartItem.isRenting();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setRenting(isRenting);
            orderItem.setBook(book);
            orderItem.setPriceAtPurchase(isRenting ? book.getRentalPrice() : book.getBuyPrice());

            orderItemService.save(orderItem);
        }

        try{
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(totalAmount*100, "INR");
            order.setPaymentId(paymentIntent.getId());
            orderRepository.save(order);
            return new CreatePaymentResponse(order.getId(),paymentIntent.getClientSecret());
        }catch (Exception e){
            e.printStackTrace();
            order.setStatus("FAILED");
            orderRepository.save(order);
            throw new RuntimeException("Payment failed");
        }
    }

    @Transactional
    public void handlePayment(String paymentIntentId) {

        if (paymentIntentId.contains("_secret_")) {
            paymentIntentId = paymentIntentId.split("_secret_")[0];
        }

        Order order = orderRepository.findByPaymentId(paymentIntentId);

        if (order == null) {
            throw new RuntimeException("Order not found for payment ID: " + paymentIntentId);
        }

        if ("PAID".equals(order.getStatus())) {
            return;
        }

        try{
            stripeService.confirmPayment(paymentIntentId);
            order.setStatus("PAID");

            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem item : orderItems) {
                Long bookId = item.getBook().getId();
                Long userId = order.getUser().getId();
                if (item.isRenting()) {
                    bookService.rentBook(userId, bookId);
                } else {
                    bookService.sellBook(userId, bookId);
                }
                orderRepository.save(order);

                cartService.clearCartByUserId(userId);
                cartService.deleteCartByUserId(userId);
            }
        }catch (Exception e){
            e.printStackTrace();
            order.setStatus("FAILED");
            orderRepository.save(order);
            throw new RuntimeException("Payment processing failed: " + e.getMessage());
        }
    }

    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return OrderMapper.toOrderDTO(order);
    }
}
