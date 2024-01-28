package com.guibaarros.fiap.postech.fastfood.domain.repository.order;

import com.guibaarros.fiap.postech.fastfood.domain.entities.order.Order;
import com.guibaarros.fiap.postech.fastfood.domain.entities.order.enums.OrderStatus;

import java.util.List;

public interface FindOrderInPreparationPort {
    List<Order> findOrderByStatusIn(List<OrderStatus> orderStatusList);
}
