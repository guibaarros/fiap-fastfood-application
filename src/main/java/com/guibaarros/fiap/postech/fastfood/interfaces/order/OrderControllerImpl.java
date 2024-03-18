package com.guibaarros.fiap.postech.fastfood.interfaces.order;

import com.guibaarros.fiap.postech.fastfood.application.dtos.client.ClientResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.dtos.order.OrderRequestDTO;
import com.guibaarros.fiap.postech.fastfood.application.dtos.order.OrderResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.client.ClientNotFoundException;
import com.guibaarros.fiap.postech.fastfood.application.usecases.client.CreateClientUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.client.FindClientByEmailUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.order.CreateOrderUseCase;
import com.guibaarros.fiap.postech.fastfood.domain.entities.client.ClientValueObject;
import com.guibaarros.fiap.postech.fastfood.interfaces.client.identification.ClientIdentificationPort;
import com.guibaarros.fiap.postech.fastfood.interfaces.client.identification.ClientIdentificationResponseDTO;
import com.guibaarros.fiap.postech.fastfood.interfaces.client.parser.ClientBearerTokenParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final ClientIdentificationPort clientIdentificationPort;
    private final ClientBearerTokenParser clientBearerTokenParser;
    private final FindClientByEmailUseCase findClientByEmailUseCase;
    private final CreateClientUseCase createClientUseCase;

    @Override
    public OrderResponseDTO createOrder(final Map<String, String> headers, final OrderRequestDTO orderRequestDTO) {
        final String authorizationValue = headers.get("authorization");
        if (Objects.nonNull(authorizationValue)) {
            // cliente identificado
            // Lê o username do token
            final String username = clientBearerTokenParser.parseTokenToSub(authorizationValue);

            // busca as informações do cliente
            final ClientValueObject clientValueObject = getClientData(username);

            // verifica se ja tem cadastrado
            final ClientResponseDTO client = getClient(clientValueObject);

            // cria pedido com id do cliente e lista de produtos
            return createOrderUseCase.createOrder(client.getId(), orderRequestDTO.getProductIds());

        }
        // cliente não identificado
        return createOrderUseCase.createOrder(orderRequestDTO.getProductIds());
    }

    private ClientValueObject getClientData(final String userName) {
        final ClientIdentificationResponseDTO clientIdentificationResponseDTO =
                clientIdentificationPort.identifyClient(userName);
        return new ClientValueObject(
                Long.parseLong(clientIdentificationResponseDTO.getCpf()),
                clientIdentificationResponseDTO.getName(),
                clientIdentificationResponseDTO.getEmail()
        );
    }

    private ClientResponseDTO getClient(final ClientValueObject clientValueObject) {
        try {
            return findClientByEmailUseCase.findClientByEmail(clientValueObject.email());
        } catch (final ClientNotFoundException e) {
            return createClientUseCase.createClient(clientValueObject);
        }
    }
}
