package com.guibaarros.fiap.postech.fastfood.interfaces.client.identification;

public class ClientIdentificationNotFoundException extends RuntimeException {

    public ClientIdentificationNotFoundException(final String username) {
        super("client not found with username: " + username);
    }
}
