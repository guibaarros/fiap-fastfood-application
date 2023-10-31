package com.guibaarros.fiap.postech.fastfood.adapters.dtos.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.enums.ProductCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductRequestDTO {

    @NotEmpty(message = "nome do produto é obrigatório")
    private String name;

    @NotNull(message = "categoria é obrigatória")
    private ProductCategory category;

    @DecimalMin(value = "0.01", message = "valor deve ser maior que 0")
    private BigDecimal price;

    @NotEmpty(message = "descrição do produto é obrigatória")
    private String description;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
