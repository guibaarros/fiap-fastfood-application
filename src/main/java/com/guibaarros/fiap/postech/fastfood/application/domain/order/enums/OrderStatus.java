package com.guibaarros.fiap.postech.fastfood.application.domain.order.enums;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    AWAITING_PAYMENT,
    AWAITING_PREPARATION,
    PREPARING,
    READY,
    FINISHED,
    CANCELLED;

    public static List<OrderStatus> getInPreparationStatuses() {
        return Arrays.asList(AWAITING_PREPARATION, PREPARING, READY);
    }
}
