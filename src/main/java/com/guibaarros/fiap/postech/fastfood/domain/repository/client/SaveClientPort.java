package com.guibaarros.fiap.postech.fastfood.domain.repository.client;

import com.guibaarros.fiap.postech.fastfood.domain.entities.client.Client;

public interface SaveClientPort {
    Client save(Client client);
}
