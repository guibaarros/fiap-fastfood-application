package com.guibaarros.fiap.postech.fastfood.application.usecases.client;

import com.guibaarros.fiap.postech.fastfood.application.dtos.client.ClientResponseDTO;

public interface FindClientByCpfUseCase {

    ClientResponseDTO findClientByCpf(Long cpf);
}
