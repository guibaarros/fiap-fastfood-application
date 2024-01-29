package com.guibaarros.fiap.postech.fastfood.application.exceptions.client;

import com.guibaarros.fiap.postech.fastfood.domain.entities.client.ClientValueObject;

public class ClientAlreadyExistsException extends RuntimeException {

    public ClientAlreadyExistsException(final ClientValueObject clienteValueObject) {
        super("já existe um cliente cadastrado com o cpf " + clienteValueObject.cpf()
                + ", nome " + clienteValueObject.name()
                + " e email " + clienteValueObject.email());
    }

    public ClientAlreadyExistsException(final Long cpf) {
        super("já existe um cliente cadastrado com o cpf " + cpf);
    }
}
