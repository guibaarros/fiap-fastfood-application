package com.guibaarros.fiap.postech.fastfood.application.port.incoming.order;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderResponseDTO;

import java.util.List;

public interface ListQueuedOrderUseCase {

    List<OrderResponseDTO> listQueuedOrderUseCase();
}
