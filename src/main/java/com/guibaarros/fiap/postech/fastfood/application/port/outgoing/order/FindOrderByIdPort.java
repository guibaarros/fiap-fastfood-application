package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order;

import com.guibaarros.fiap.postech.fastfood.application.domain.order.Order;

import java.util.Optional;

public interface FindOrderByIdPort {
    Optional<Order> findOrderById(Long id);
}
