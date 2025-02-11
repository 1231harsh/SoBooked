package com.hvrc.bookStore.service;

import com.hvrc.bookStore.dto.CreatePaymentResponse;
import com.hvrc.bookStore.dto.OrderDTO;
import com.hvrc.bookStore.dto.OrderMapper;
import com.hvrc.bookStore.model.*;
import com.hvrc.bookStore.repository.OrderRepository;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private BookService bookService;

    @Autowired
    private StripeService stripeService;

    @Transactional
    public CreatePaymentResponse placeOrder(Long cartId) {
        Cart cart = cartService.getCartById(cartId);
        Long userId = cart.getUser().getId();

        List<CartItems> cartItems = cart.getCartItems();

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.isRenting() ? item.getBook().getRentalPrice() : item.getBook().getBuyPrice())
                .sum();

        Order order = new Order();
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
            orderItem.setProductId(cartItem.getBook().getId());
            orderItem.setPriceAtPurchase(isRenting ? book.getRentalPrice() : book.getBuyPrice());

            if (isRenting) {
                book.setAvailableForRent(false);
                bookService.rentBook(userId,book.getId());
            } else {
                bookService.sellBook(userId,book.getId());
            }
            orderItemService.save(orderItem);
        }

        try{
            PaymentIntent paymentIntent = stripeService.createPaymentIntent(totalAmount*100, "USD");
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
        Order order = orderRepository.findByPaymentId(paymentIntentId);
        try{
            stripeService.confirmPayment(paymentIntentId);
            order.setStatus("PAID");
            orderRepository.save(order);
        }catch (Exception e){
            e.printStackTrace();
            order.setStatus("FAILED");
        }
        orderRepository.save(order);
    }

    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        OrderDTO orderDTO = OrderMapper.toOrderDTO(order);
        return orderDTO;
    }
}
