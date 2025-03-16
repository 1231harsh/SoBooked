package com.hvrc.bookStore.repository;

import com.hvrc.bookStore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    void deleteByBookId(Long bookId);
}
