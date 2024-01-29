package com.guibaarros.fiap.postech.fastfood.application.usecases.order;

import com.guibaarros.fiap.postech.fastfood.application.dtos.order.OrderResponseDTO;

import java.util.List;

public interface ListQueuedOrderUseCase {

    List<OrderResponseDTO> listQueuedOrderUseCase();
}
