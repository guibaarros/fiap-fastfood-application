package com.guibaarros.fiap.postech.fastfood.domain.entities.order.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum OrderStatus {
    AWAITING_PAYMENT(-1),
    AWAITING_PREPARATION(-1),
    PREPARING(1),
    READY(0),
    RECEIVED(2),
    FINISHED(-1),
    CANCELLED(-1);

    private final Integer presentationOrder;

    OrderStatus(final Integer presentationOrder) {
        this.presentationOrder = presentationOrder;
    }

    public static List<OrderStatus> getInPreparationStatuses() {
        return Arrays.asList(RECEIVED, PREPARING, READY);
    }
}
