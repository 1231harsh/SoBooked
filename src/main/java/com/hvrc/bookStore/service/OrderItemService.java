package com.hvrc.bookStore.service;

import com.hvrc.bookStore.entity.OrderItem;
import com.hvrc.bookStore.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Transactional
    public void deleteOrderItem(Long BookId) {
        orderItemRepository.deleteByBookId(BookId);
    }
}
