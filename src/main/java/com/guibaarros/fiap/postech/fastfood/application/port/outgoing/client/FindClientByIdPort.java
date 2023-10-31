package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.client;

import com.guibaarros.fiap.postech.fastfood.application.domain.client.Client;

import java.util.Optional;

public interface FindClientByIdPort {
    Optional<Client> findById(Long id);
}
