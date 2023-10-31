package com.guibaarros.fiap.postech.fastfood.application.port.incoming.product;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.product.ProductResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.ProductValueObject;

import java.io.IOException;

public interface CreateProductUseCase {

    ProductResponseDTO createProduct(final ProductValueObject productValueObject) throws IOException;
}
