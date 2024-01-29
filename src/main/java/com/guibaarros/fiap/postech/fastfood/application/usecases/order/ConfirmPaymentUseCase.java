package com.guibaarros.fiap.postech.fastfood.application.usecases.order;

public interface ConfirmPaymentUseCase {

    void confirmPayment(Long externalId, String status);
}
