package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order;

import com.guibaarros.fiap.postech.fastfood.application.domain.order.Order;

public interface SaveOrderPort {
    Order saveOrder(Order order);
}
