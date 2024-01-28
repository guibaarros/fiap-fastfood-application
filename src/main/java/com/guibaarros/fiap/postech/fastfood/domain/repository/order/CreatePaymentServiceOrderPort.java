package com.guibaarros.fiap.postech.fastfood.domain.repository.order;

import com.guibaarros.fiap.postech.fastfood.infrastructure.web.httpclient.dto.PaymentServiceResponseDTO;

import java.math.BigDecimal;

public interface CreatePaymentServiceOrderPort {
    PaymentServiceResponseDTO createPaymentServiceOrder(Long id, BigDecimal totalAmount);
}
