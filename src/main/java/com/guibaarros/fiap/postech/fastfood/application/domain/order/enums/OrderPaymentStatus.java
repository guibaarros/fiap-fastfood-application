package com.guibaarros.fiap.postech.fastfood.application.domain.order.enums;

import lombok.Getter;

@Getter
public enum OrderPaymentStatus {
    AWAITING_PAYMENT(false),
    PAID(true),
    CANCELLED(false);

    private final boolean paymentApproved;

    OrderPaymentStatus(boolean paymentApproved) {
        this.paymentApproved = paymentApproved;
    }
}
