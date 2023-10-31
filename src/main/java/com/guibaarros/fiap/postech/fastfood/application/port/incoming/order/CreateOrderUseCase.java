package com.guibaarros.fiap.postech.fastfood.application.port.incoming.order;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderResponseDTO;

import java.util.List;

public interface CreateOrderUseCase {

    OrderResponseDTO createOrder(final Long clientId, final List<Long> productIds);

    OrderResponseDTO createOrder(final List<Long> productIds);
}
