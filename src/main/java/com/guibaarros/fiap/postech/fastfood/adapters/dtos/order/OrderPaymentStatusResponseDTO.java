package com.guibaarros.fiap.postech.fastfood.adapters.dtos.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.enums.OrderPaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderPaymentStatusResponseDTO {

    private Long id;
    private OrderPaymentStatus paymentStatus;
    private LocalDateTime paymentStatusUpdatedAt;
    private Boolean isPaymentApproved;
}
