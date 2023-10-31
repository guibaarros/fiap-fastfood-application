package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order;

import com.guibaarros.fiap.postech.fastfood.application.domain.order.Order;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.enums.OrderStatus;

import java.util.List;

public interface FindOrderInPreparationPort {
    List<Order> findOrderByStatusIn(List<OrderStatus> orderStatusList);
}
