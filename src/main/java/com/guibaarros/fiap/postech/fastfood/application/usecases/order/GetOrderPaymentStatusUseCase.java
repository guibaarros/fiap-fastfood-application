package com.guibaarros.fiap.postech.fastfood.application.usecases.order;

import com.guibaarros.fiap.postech.fastfood.application.dtos.order.OrderPaymentStatusResponseDTO;

public interface GetOrderPaymentStatusUseCase {

    OrderPaymentStatusResponseDTO getOrderPaymentByOrderId(Long orderId);
}
