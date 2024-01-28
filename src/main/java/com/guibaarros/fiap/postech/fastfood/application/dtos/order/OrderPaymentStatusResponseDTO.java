package com.guibaarros.fiap.postech.fastfood.application.dtos.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.guibaarros.fiap.postech.fastfood.domain.entities.order.enums.OrderPaymentStatus;
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
