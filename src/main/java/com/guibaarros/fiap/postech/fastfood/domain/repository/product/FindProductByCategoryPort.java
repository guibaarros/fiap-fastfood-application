package com.guibaarros.fiap.postech.fastfood.domain.repository.product;

import com.guibaarros.fiap.postech.fastfood.domain.entities.product.Product;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.enums.ProductCategory;

import java.util.List;

public interface FindProductByCategoryPort {
    List<Product> findProductByCategory(ProductCategory productCategory);
}
