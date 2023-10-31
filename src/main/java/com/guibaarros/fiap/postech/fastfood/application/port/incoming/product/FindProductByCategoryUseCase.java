package com.guibaarros.fiap.postech.fastfood.application.port.incoming.product;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.product.ProductResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.enums.ProductCategory;

import java.util.List;

public interface FindProductByCategoryUseCase {

    List<ProductResponseDTO> findProductByCategory(ProductCategory productCategory);
}
