package com.hvrc.bookStore.dto;

import com.hvrc.bookStore.entity.Order;

public class OrderMapper {

    public static OrderDTO toOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setCustomerId(order.getUser().getId());
        orderDTO.setPaymentId(order.getPaymentId());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setStatus(order.getStatus());
        return orderDTO;
    }
}
