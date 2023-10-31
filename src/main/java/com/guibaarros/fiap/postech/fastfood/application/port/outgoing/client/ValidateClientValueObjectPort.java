package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.client;

import com.guibaarros.fiap.postech.fastfood.application.domain.client.ClientValueObject;

public interface ValidateClientValueObjectPort {
    boolean existsClientValueObject(ClientValueObject clientValueObject);
}
