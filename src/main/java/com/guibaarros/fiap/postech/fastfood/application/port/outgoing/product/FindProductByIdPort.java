package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.product;

import com.guibaarros.fiap.postech.fastfood.application.domain.product.Product;

import java.util.Optional;

public interface FindProductByIdPort {
    Optional<Product> findProductById(Long id);
}
