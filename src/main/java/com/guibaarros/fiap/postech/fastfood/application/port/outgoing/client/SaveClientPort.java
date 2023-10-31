package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.client;

import com.guibaarros.fiap.postech.fastfood.application.domain.client.Client;

public interface SaveClientPort {
    Client save(Client client);
}
