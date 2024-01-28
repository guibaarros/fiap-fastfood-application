package com.guibaarros.fiap.postech.fastfood.application.usecases.client;

import com.guibaarros.fiap.postech.fastfood.application.dtos.client.ClientResponseDTO;
import com.guibaarros.fiap.postech.fastfood.domain.entities.client.ClientValueObject;

public interface CreateClientUseCase {

    ClientResponseDTO createClient(final ClientValueObject clientValueObject);
}
