package com.guibaarros.fiap.postech.fastfood.application.exceptions.product;

import com.guibaarros.fiap.postech.fastfood.application.domain.product.enums.ProductCategory;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(final ProductCategory productCategory) {
        super("produto não encontrado com a categoria " + productCategory);
    }

    public ProductNotFoundException(final Long id) {
        super("produto não encontrado com o id " + id);
    }
}
