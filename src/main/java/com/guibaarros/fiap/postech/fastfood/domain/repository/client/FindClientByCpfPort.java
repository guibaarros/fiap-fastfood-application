package com.guibaarros.fiap.postech.fastfood.domain.repository.client;

import com.guibaarros.fiap.postech.fastfood.domain.entities.client.Client;

import java.util.Optional;

public interface FindClientByCpfPort {
    Optional<Client> findByCpf(Long cpf);
}
