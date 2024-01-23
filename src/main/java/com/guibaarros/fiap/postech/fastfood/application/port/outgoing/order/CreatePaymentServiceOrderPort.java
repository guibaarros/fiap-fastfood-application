package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order;

import com.guibaarros.fiap.postech.fastfood.adapters.httpclient.dto.PaymentServiceResponseDTO;

import java.math.BigDecimal;

public interface CreatePaymentServiceOrderPort {
    PaymentServiceResponseDTO createPaymentServiceOrder(Long id, BigDecimal totalAmount);
}
