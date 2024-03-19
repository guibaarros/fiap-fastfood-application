package com.guibaarros.fiap.postech.fastfood.interfaces.order;

import com.guibaarros.fiap.postech.fastfood.application.dtos.order.OrderRequestDTO;
import com.guibaarros.fiap.postech.fastfood.application.dtos.order.OrderResponseDTO;

import java.util.Map;

public interface OrderController {

    OrderResponseDTO createOrder(final Map<String, String> headers, final OrderRequestDTO orderRequestDTO);
}
