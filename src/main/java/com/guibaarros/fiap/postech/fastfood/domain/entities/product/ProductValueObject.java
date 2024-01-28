package com.guibaarros.fiap.postech.fastfood.domain.entities.product;

import com.guibaarros.fiap.postech.fastfood.domain.entities.product.enums.ProductCategory;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record ProductValueObject(
        String name,
        ProductCategory category,
        BigDecimal price,
        String description,
        MultipartFile image) {
}
