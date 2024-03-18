package com.guibaarros.fiap.postech.fastfood.application.services;

import com.guibaarros.fiap.postech.fastfood.application.dtos.client.ClientResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.client.ClientAlreadyExistsException;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.client.ClientNotFoundException;
import com.guibaarros.fiap.postech.fastfood.application.usecases.client.CreateClientUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.client.FindClientByCpfUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.client.FindClientByEmailUseCase;
import com.guibaarros.fiap.postech.fastfood.domain.entities.client.Client;
import com.guibaarros.fiap.postech.fastfood.domain.entities.client.ClientValueObject;
import com.guibaarros.fiap.postech.fastfood.domain.repository.client.FindClientByCpfPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.client.FindClientByEmailPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.client.FindClientByIdPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.client.SaveClientPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.client.ValidateClientValueObjectPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService implements CreateClientUseCase, FindClientByCpfUseCase, FindClientByEmailUseCase {

    private final SaveClientPort saveClientPort;
    private final FindClientByCpfPort findClientByCpfPort;
    private final FindClientByIdPort findClientByIdPort;
    private final FindClientByEmailPort findClientByEmailPort;
    private final ValidateClientValueObjectPort validateClientValueObjectPort;

    @Override
    public ClientResponseDTO createClient(final ClientValueObject clientValueObject) {
        if (Objects.nonNull(clientValueObject.cpf()) &&
                findClientByCpfPort.findByCpf(clientValueObject.cpf()).isPresent()) {
            final ClientAlreadyExistsException clientAlreadyExistsException =
                    new ClientAlreadyExistsException(clientValueObject.cpf());
            log.error("create new client; clientValueObject={}", clientValueObject, clientAlreadyExistsException);
            throw clientAlreadyExistsException;
        }

        if (validateClientValueObjectPort.existsClientValueObject(clientValueObject)) {
            final ClientAlreadyExistsException clientAlreadyExistsException =
                    new ClientAlreadyExistsException(clientValueObject);
            log.error("create new client; clientValueObject={}", clientValueObject, clientAlreadyExistsException);
            throw clientAlreadyExistsException;
        }

        final Client persistedClient = saveClientPort.save(mapValueObjectToEntity(clientValueObject));
        log.info("client created succesfully;");
        return mapEntityToResponseDto(persistedClient);
    }

    @Override
    public ClientResponseDTO findClientByCpf(final Long cpf) {
        final Optional<Client> optClient = findClientByCpfPort.findByCpf(cpf);
        if (optClient.isPresent()) {
            return mapEntityToResponseDto(optClient.get());
        }

        final ClientNotFoundException clientNotFoundException = new ClientNotFoundException(cpf.toString());
        log.error("client not found; cpf={}", cpf, clientNotFoundException);
        throw clientNotFoundException;
    }

    @Override
    public ClientResponseDTO findClientByEmail(final String email) {
        final Optional<Client> optClient = findClientByEmailPort.findByEmail(email);
        if (optClient.isPresent()) {
            return mapEntityToResponseDto(optClient.get());
        }

        final ClientNotFoundException clientNotFoundException = new ClientNotFoundException(email);
        log.error("client not found; email={}", email, clientNotFoundException);
        throw clientNotFoundException;
    }

    private Client mapValueObjectToEntity(final ClientValueObject clientValueObject) {
        return new Client(
                clientValueObject.cpf(),
                clientValueObject.name(),
                clientValueObject.email()
        );
    }

    protected ClientResponseDTO mapEntityToResponseDto(final Client client) {
        final ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setId(client.getId());
        clientResponseDTO.setCpf(client.getCpf());
        clientResponseDTO.setName(client.getName());
        clientResponseDTO.setEmail(client.getEmail());
        clientResponseDTO.setLastVisit(client.getLastVisit());
        clientResponseDTO.setCreatedAt(client.getCreatedAt());
        return clientResponseDTO;
    }

    protected Client findClientById(final Long id) {
        final Optional<Client> optClient = findClientByIdPort.findById(id);
        if (optClient.isPresent()) {
            return optClient.get();
        }

        final ClientNotFoundException clientNotFoundException = new ClientNotFoundException(id.toString());
        log.error("client not found; id={}", id, clientNotFoundException);
        throw clientNotFoundException;
    }
}
