package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.entity.Order;
import com.hvrc.bookStore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByPaymentId(String paymentIntentId);

    List<Order> findByUser(User user);
}
