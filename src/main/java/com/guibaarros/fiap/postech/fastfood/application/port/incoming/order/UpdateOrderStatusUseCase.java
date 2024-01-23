package com.guibaarros.fiap.postech.fastfood.application.port.incoming.order;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderResponseDTO;

public interface UpdateOrderStatusUseCase {

    OrderResponseDTO updateOrderStatus(Long id, String status);
}
