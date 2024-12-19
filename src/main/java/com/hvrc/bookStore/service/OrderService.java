package com.hvrc.bookStore.service;

import com.hvrc.bookStore.dto.CreatePaymentResponse;
import com.hvrc.bookStore.dto.OrderDTO;
import com.hvrc.bookStore.dto.OrderMapper;
import com.hvrc.bookStore.model.Cart;
import com.hvrc.bookStore.model.CartItems;
import com.hvrc.bookStore.model.Order;
import com.hvrc.bookStore.model.OrderItem;
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
        List<CartItems> cartItems = cart.getCartItems();

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getBook().getPrice())
                .sum();

        Order order = new Order();
        order.setTotalAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        orderRepository.save(order);

        for (CartItems cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(cartItem.getBook().getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getBook().getPrice());

<<<<<<< HEAD
            System.out.println("update book call hua");
=======
//            System.out.println("update book call hua");
>>>>>>> f00bc12 (Added new files to the project)
            bookService.updateBook(cartItem.getBook().getId(), cartItem.getQuantity());

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
<<<<<<< HEAD
        Order order = orderRepository.findById(orderId).get();
        OrderDTO orderDTO = OrderMapper.toOrderDTO(order);
        return orderDTO;
=======
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return OrderMapper.toOrderDTO(order);
>>>>>>> f00bc12 (Added new files to the project)
    }
}
