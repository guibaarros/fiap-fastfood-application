package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.product;

import com.guibaarros.fiap.postech.fastfood.application.domain.product.Product;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.enums.ProductCategory;

import java.util.List;

public interface FindProductByCategoryPort {
    List<Product> findProductByCategory(ProductCategory productCategory);
}
