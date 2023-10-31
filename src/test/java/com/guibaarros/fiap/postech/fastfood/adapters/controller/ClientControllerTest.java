package com.guibaarros.fiap.postech.fastfood.adapters.controller;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.client.ClientRequestDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.client.ClientResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.domain.client.ClientValueObject;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.client.CreateClientUseCase;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.client.FindClientByCpfUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private CreateClientUseCase createClientUseCase;

    @Mock
    private FindClientByCpfUseCase findClientByCpfUseCase;

    @InjectMocks
    private ClientController clientController;

    @Test
    void createClient() {
        final Long cpf = 54281733000L;
        final String name = "Guilherme";
        final String email = "guilherme@gmail.com";

        final ClientValueObject clientValueObject = new ClientValueObject(
                cpf,
                name,
                email
        );

        final ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setCpf(cpf);
        clientRequestDTO.setName(name);
        clientRequestDTO.setEmail(email);

        final ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setCpf(cpf);
        clientResponseDTO.setName(name);
        clientResponseDTO.setEmail(email);

        final ResponseEntity<ClientResponseDTO> expectedResponse =
                ResponseEntity.status(HttpStatus.CREATED).body(clientResponseDTO);

        Mockito.when(createClientUseCase.createClient(Mockito.eq(clientValueObject))).thenReturn(clientResponseDTO);

        final ResponseEntity<ClientResponseDTO> actualResponse = clientController.createClient(clientRequestDTO);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void findClientByCpf() {
        final Long cpf = 54281733000L;
        final String name = "Guilherme";
        final String email = "guilherme@gmail.com";

        final ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setCpf(cpf);
        clientResponseDTO.setName(name);
        clientResponseDTO.setEmail(email);

        final ResponseEntity<ClientResponseDTO> expectedResponse =
                ResponseEntity.status(HttpStatus.CREATED).body(clientResponseDTO);

        Mockito.when(findClientByCpfUseCase.findClientByCpf(Mockito.eq(cpf))).thenReturn(clientResponseDTO);

        final ResponseEntity<ClientResponseDTO> actualResponse = clientController.findClientByCpf(cpf);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }
}