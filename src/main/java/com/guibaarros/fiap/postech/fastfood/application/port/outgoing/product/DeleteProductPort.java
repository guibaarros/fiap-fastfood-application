package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.product;

import com.guibaarros.fiap.postech.fastfood.application.domain.product.Product;

public interface DeleteProductPort {
    void delete(Product product);
}
