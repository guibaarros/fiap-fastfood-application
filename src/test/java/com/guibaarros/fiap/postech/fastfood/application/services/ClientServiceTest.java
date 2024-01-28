package com.guibaarros.fiap.postech.fastfood.application.services;

import com.guibaarros.fiap.postech.fastfood.application.dtos.client.ClientResponseDTO;
import com.guibaarros.fiap.postech.fastfood.domain.entities.client.Client;
import com.guibaarros.fiap.postech.fastfood.domain.entities.client.ClientValueObject;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.client.ClientAlreadyExistsException;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.client.ClientNotFoundException;
import com.guibaarros.fiap.postech.fastfood.domain.repository.client.FindClientByCpfPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.client.FindClientByIdPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.client.SaveClientPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.client.ValidateClientValueObjectPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private SaveClientPort saveClientPort;

    @Mock
    private FindClientByCpfPort findClientByCpfPort;

    @Mock
    private FindClientByIdPort findClientByIdPort;

    @Mock
    private ValidateClientValueObjectPort validateClientValueObjectPort;

    @InjectMocks
    private ClientService clientService;

    @Test
    void createClientThatAlreadyExistsByCPF() {
        final Long cpf = 1512055000L;
        final String name = "Guilherme";
        final String email = "guilherme@gmail.com";

        final ClientValueObject clientValueObject = new ClientValueObject(
                cpf,
                name,
                email
        );

        final Client alreadyPersistedClient = new Client(
                cpf,
                name,
                email
        );

        Mockito.when(findClientByCpfPort.findByCpf(Mockito.any())).thenReturn(Optional.of(alreadyPersistedClient));

        Assertions.assertThrows(ClientAlreadyExistsException.class,
                () -> clientService.createClient(clientValueObject));
    }

    @Test
    void createClientThatAlreadyExistsByValueObject() {
        final Long cpf = 1512055000L;
        final String name = "Guilherme";
        final String email = "guilherme@gmail.com";

        final ClientValueObject clientValueObject = new ClientValueObject(
                cpf,
                name,
                email
        );

        Mockito.when(validateClientValueObjectPort.existsClientValueObject(Mockito.eq(clientValueObject)))
                        .thenReturn(Boolean.TRUE);

        Assertions.assertThrows(ClientAlreadyExistsException.class,
                () -> clientService.createClient(clientValueObject));
    }

    @Test
    void createClient() {
        final Long cpf = 1512055000L;
        final String name = "Guilherme";
        final String email = "guilherme@gmail.com";

        final ClientValueObject clientValueObject = new ClientValueObject(
                cpf,
                name,
                email
        );

        final Client persistedClient = new Client(
                cpf,
                name,
                email
        );

        final ClientResponseDTO expectedClientResponseDTO = new ClientResponseDTO();
        expectedClientResponseDTO.setCpf(cpf);
        expectedClientResponseDTO.setName(name);
        expectedClientResponseDTO.setEmail(email);

        Mockito.when(findClientByCpfPort.findByCpf(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(validateClientValueObjectPort.existsClientValueObject(Mockito.eq(clientValueObject)))
                .thenReturn(Boolean.FALSE);
        Mockito.when(saveClientPort.save(Mockito.any())).thenReturn(persistedClient);

        final ClientResponseDTO actualClientDto = clientService.createClient(clientValueObject);

        Assertions.assertEquals(expectedClientResponseDTO.getName(), actualClientDto.getName());
        Assertions.assertEquals(expectedClientResponseDTO.getEmail(), actualClientDto.getEmail());
        Assertions.assertEquals(expectedClientResponseDTO.getCpf(), actualClientDto.getCpf());
    }

    @Test
    void findClientByCpf() {
        final Long cpf = 1512055000L;
        final String name = "Guilherme";
        final String email = "guilherme@gmail.com";

        final Client alreadyPersistedClient = new Client(
                cpf,
                name,
                email
        );

        final ClientResponseDTO expectedClientResponseDTO = new ClientResponseDTO();
        expectedClientResponseDTO.setCpf(cpf);
        expectedClientResponseDTO.setName(name);
        expectedClientResponseDTO.setEmail(email);

        Mockito.when(findClientByCpfPort.findByCpf(Mockito.any())).thenReturn(Optional.of(alreadyPersistedClient));

        final ClientResponseDTO actualClientDto = clientService.findClientByCpf(cpf);

        Assertions.assertEquals(expectedClientResponseDTO.getName(), actualClientDto.getName());
        Assertions.assertEquals(expectedClientResponseDTO.getEmail(), actualClientDto.getEmail());
        Assertions.assertEquals(expectedClientResponseDTO.getCpf(), actualClientDto.getCpf());
    }

    @Test
    void findClientByCpfNotFound() {
        final Long cpf = 1512055000L;

        Mockito.when(findClientByCpfPort.findByCpf(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(ClientNotFoundException.class,
                () -> clientService.findClientByCpf(cpf));
    }

    @Test
    void findClientById() {
        final Long id = 1L;
        final Long cpf = 1512055000L;
        final String name = "Guilherme";
        final String email = "guilherme@gmail.com";

        final Client alreadyPersistedClient = new Client(
                cpf,
                name,
                email
        );

        final Client expected = new Client(
                cpf,
                name,
                email
        );

        Mockito.when(findClientByIdPort.findById(Mockito.any())).thenReturn(Optional.of(alreadyPersistedClient));

        final Client actualClient = clientService.findClientById(id);

        Assertions.assertEquals(expected.getName(), actualClient.getName());
        Assertions.assertEquals(expected.getEmail(), actualClient.getEmail());
        Assertions.assertEquals(expected.getCpf(), actualClient.getCpf());
    }

    @Test
    void findClientByIdNotFound() {
        final Long id = 1L;

        Mockito.when(findClientByIdPort.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(ClientNotFoundException.class,
                () -> clientService.findClientById(id));
    }
}