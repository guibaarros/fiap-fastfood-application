package com.guibaarros.fiap.postech.fastfood.application.usecases.product;

import com.guibaarros.fiap.postech.fastfood.application.dtos.product.ProductResponseDTO;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.enums.ProductCategory;

import java.util.List;

public interface FindProductByCategoryUseCase {

    List<ProductResponseDTO> findProductByCategory(ProductCategory productCategory);
}
