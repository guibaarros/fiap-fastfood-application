package com.guibaarros.fiap.postech.fastfood.domain.repository.order;

import com.guibaarros.fiap.postech.fastfood.domain.entities.order.Order;

public interface SaveOrderPort {
    Order saveOrder(Order order);
}
