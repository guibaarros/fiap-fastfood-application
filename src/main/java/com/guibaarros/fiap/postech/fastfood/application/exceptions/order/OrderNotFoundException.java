package com.guibaarros.fiap.postech.fastfood.application.exceptions.order;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(final Long id) {
        super("pedido não encontrado com o id " + id);
    }

    public OrderNotFoundException(final String message) {
        super(message);
    }

}
