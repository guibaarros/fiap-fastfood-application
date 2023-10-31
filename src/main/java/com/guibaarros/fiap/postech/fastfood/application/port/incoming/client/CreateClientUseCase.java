package com.guibaarros.fiap.postech.fastfood.application.port.incoming.client;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.client.ClientResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.domain.client.ClientValueObject;

public interface CreateClientUseCase {

    ClientResponseDTO createClient(final ClientValueObject clientValueObject);
}
