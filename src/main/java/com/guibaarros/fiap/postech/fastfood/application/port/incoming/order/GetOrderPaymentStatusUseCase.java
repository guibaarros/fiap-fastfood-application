package com.guibaarros.fiap.postech.fastfood.application.port.incoming.order;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderPaymentStatusResponseDTO;

public interface GetOrderPaymentStatusUseCase {

    OrderPaymentStatusResponseDTO getOrderPaymentByOrderId(Long orderId);
}
