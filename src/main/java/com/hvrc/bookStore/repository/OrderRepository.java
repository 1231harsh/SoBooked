package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByPaymentId(String paymentIntentId);
}
