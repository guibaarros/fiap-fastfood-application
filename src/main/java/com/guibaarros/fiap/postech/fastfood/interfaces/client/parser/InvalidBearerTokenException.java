package com.guibaarros.fiap.postech.fastfood.interfaces.client.parser;

public class InvalidBearerTokenException extends RuntimeException {

    public InvalidBearerTokenException() {
        super("invalid bearer token to be parsed");
    }
}
