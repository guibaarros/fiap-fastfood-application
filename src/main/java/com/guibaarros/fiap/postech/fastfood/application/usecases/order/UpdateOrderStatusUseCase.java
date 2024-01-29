package com.guibaarros.fiap.postech.fastfood.application.usecases.order;

import com.guibaarros.fiap.postech.fastfood.application.dtos.order.OrderResponseDTO;

public interface UpdateOrderStatusUseCase {

    OrderResponseDTO updateOrderStatus(Long id, String status);
}
