package com.guibaarros.fiap.postech.fastfood.application.usecases.client;

import com.guibaarros.fiap.postech.fastfood.application.dtos.client.ClientResponseDTO;

public interface FindClientByEmailUseCase {
    ClientResponseDTO findClientByEmail(String email);
}
