package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.product;

import com.guibaarros.fiap.postech.fastfood.application.domain.product.Product;

public interface SaveProductPort {
    Product save(Product product);
}
