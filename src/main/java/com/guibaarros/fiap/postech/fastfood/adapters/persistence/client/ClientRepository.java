package com.guibaarros.fiap.postech.fastfood.adapters.persistence.client;

import com.guibaarros.fiap.postech.fastfood.application.domain.client.Client;
import com.guibaarros.fiap.postech.fastfood.application.domain.client.ClientValueObject;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.client.FindClientByCpfPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.client.FindClientByIdPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.client.SaveClientPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.client.ValidateClientValueObjectPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ClientRepository implements
        SaveClientPort,
        FindClientByCpfPort,
        ValidateClientValueObjectPort,
        FindClientByIdPort {

    private final ClientJpaRepository repository;

    @Override
    public Optional<Client> findByCpf(final Long cpf) {
        return repository.findByCpf(cpf);
    }

    @Override
    public Client save(final Client client) {
        return repository.save(client);
    }

    @Override
    public boolean existsClientValueObject(ClientValueObject clientValueObject) {
        return repository.existsByCpfAndNameAndEmail(
                clientValueObject.cpf(),
                clientValueObject.name(),
                clientValueObject.email()
        );
    }

    @Override
    public Optional<Client> findById(Long id) {
        return repository.findById(id);
    }
}
