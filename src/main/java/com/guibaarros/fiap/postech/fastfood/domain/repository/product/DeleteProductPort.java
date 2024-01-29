package com.guibaarros.fiap.postech.fastfood.domain.repository.product;

import com.guibaarros.fiap.postech.fastfood.domain.entities.product.Product;

public interface DeleteProductPort {
    void delete(Product product);
}
