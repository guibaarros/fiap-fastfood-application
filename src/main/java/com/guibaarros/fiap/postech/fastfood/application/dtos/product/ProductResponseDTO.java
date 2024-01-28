package com.guibaarros.fiap.postech.fastfood.application.dtos.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.enums.ProductCategory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponseDTO {

    private Long id;
    private String name;
    private ProductCategory category;
    private BigDecimal price;
    private String description;
    private LocalDateTime createdAt;

}
