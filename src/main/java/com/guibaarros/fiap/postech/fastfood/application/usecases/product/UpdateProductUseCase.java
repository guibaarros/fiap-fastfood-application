package com.guibaarros.fiap.postech.fastfood.application.usecases.product;

import com.guibaarros.fiap.postech.fastfood.application.dtos.product.ProductResponseDTO;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.ProductValueObject;

import java.io.IOException;

public interface UpdateProductUseCase {
    ProductResponseDTO update(Long id, ProductValueObject product) throws IOException;
}
