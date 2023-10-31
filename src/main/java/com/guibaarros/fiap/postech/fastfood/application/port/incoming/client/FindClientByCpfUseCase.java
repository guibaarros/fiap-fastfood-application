package com.guibaarros.fiap.postech.fastfood.application.port.incoming.client;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.client.ClientResponseDTO;

public interface FindClientByCpfUseCase {

    ClientResponseDTO findClientByCpf(Long cpf);
}
