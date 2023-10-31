package com.guibaarros.fiap.postech.fastfood.application.exceptions.order;

public class InvalidOrderOperationException extends RuntimeException {

    public InvalidOrderOperationException(final String message) {
        super(message);
    }

}
