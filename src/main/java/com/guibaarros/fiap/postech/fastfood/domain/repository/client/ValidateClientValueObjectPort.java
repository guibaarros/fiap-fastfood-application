package com.guibaarros.fiap.postech.fastfood.domain.repository.client;

import com.guibaarros.fiap.postech.fastfood.domain.entities.client.ClientValueObject;

public interface ValidateClientValueObjectPort {
    boolean existsClientValueObject(ClientValueObject clientValueObject);
}
