package com.guibaarros.fiap.postech.fastfood.domain.repository.product;

import com.guibaarros.fiap.postech.fastfood.domain.entities.product.Product;

import java.util.Optional;

public interface FindProductByIdPort {
    Optional<Product> findProductById(Long id);
}
