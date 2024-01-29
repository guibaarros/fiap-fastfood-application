package com.guibaarros.fiap.postech.fastfood.application.usecases.product;

import com.guibaarros.fiap.postech.fastfood.application.dtos.product.ProductResponseDTO;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.ProductValueObject;

import java.io.IOException;

public interface CreateProductUseCase {

    ProductResponseDTO createProduct(final ProductValueObject productValueObject) throws IOException;
}
