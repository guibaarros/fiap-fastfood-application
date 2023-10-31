package com.guibaarros.fiap.postech.fastfood.application.exceptions.product;

public class InvalidProductCategoryException extends RuntimeException {

    public InvalidProductCategoryException(final String category) {
        super("categoria " + category + " inválida");
    }
}
