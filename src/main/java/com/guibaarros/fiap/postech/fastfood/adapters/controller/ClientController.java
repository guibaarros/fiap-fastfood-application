package com.guibaarros.fiap.postech.fastfood.adapters.controller;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.client.ClientRequestDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.client.ClientResponseDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.errorhandler.ErrorDTO;
import com.guibaarros.fiap.postech.fastfood.application.domain.client.ClientValueObject;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.client.CreateClientUseCase;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.client.FindClientByCpfUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
@Tag(name = "Client Controller")
@Slf4j
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final FindClientByCpfUseCase findClientByCpfUseCase;

    @Operation(summary = "Cadastrar um novo cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Cliente cadastrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientResponseDTO.class))}),
            @ApiResponse(responseCode = "422",
                    description = "Cliente já cadastrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody final ClientRequestDTO clientRequestDTO) {
        log.info("create new client; clientRequestDTO={}", clientRequestDTO);
        final ClientValueObject clientValueObject = new ClientValueObject(
                clientRequestDTO.getCpf(),
                clientRequestDTO.getName(),
                clientRequestDTO.getEmail()
        );
        final ClientResponseDTO clientResponseDTO = createClientUseCase.createClient(clientValueObject);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientResponseDTO);
    }

    @Operation(summary = "Buscar um cliente por cpf")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Cliente encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientResponseDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Cliente não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientResponseDTO> findClientByCpf(@PathParam("cpf") final Long cpf) {
        log.info("find client by cpf; cpf={}", cpf);
        final ClientResponseDTO clientResponseDTO = findClientByCpfUseCase.findClientByCpf(cpf);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientResponseDTO);
    }
}
