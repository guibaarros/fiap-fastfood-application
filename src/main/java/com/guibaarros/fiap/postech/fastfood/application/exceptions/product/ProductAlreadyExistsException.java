package com.guibaarros.fiap.postech.fastfood.application.exceptions.product;

import com.guibaarros.fiap.postech.fastfood.application.domain.product.ProductValueObject;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(final ProductValueObject productValueObject) {
        super("já existe um produto cadastrado com o nome " + productValueObject.name()
                + ", categoria " + productValueObject.category()
                + ", preço " + productValueObject.price()
                + " e descrição " + productValueObject.description());
    }
}
