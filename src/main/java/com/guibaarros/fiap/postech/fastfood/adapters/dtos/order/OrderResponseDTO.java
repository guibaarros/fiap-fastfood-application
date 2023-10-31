package com.guibaarros.fiap.postech.fastfood.adapters.dtos.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.client.ClientResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.enums.OrderPaymentStatus;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.enums.OrderStatus;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.product.ProductResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderResponseDTO {

    private Long id;
    private ClientResponseDTO client;
    private List<ProductResponseDTO> products;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private OrderPaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
    private LocalDateTime updatedAt;
    private Long waitingTimeInMinutes;
    private String formattedNumber;

}
