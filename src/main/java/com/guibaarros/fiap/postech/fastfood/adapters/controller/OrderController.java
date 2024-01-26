package com.guibaarros.fiap.postech.fastfood.adapters.controller;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.errorhandler.ErrorDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderPaymentStatusResponseDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderRequestDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderResponseDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.UpdateOrderStatusDTO;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.order.CreateOrderUseCase;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.order.GetOrderPaymentStatusUseCase;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.order.ListQueuedOrderUseCase;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.order.UpdateOrderStatusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
@Tag(name = "Order Controller")
@Validated
@Slf4j
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final ListQueuedOrderUseCase listQueuedOrderUseCase;
    private final GetOrderPaymentStatusUseCase getOrderPaymentStatusUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Operation(summary = "Checkout do pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Pedido criado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDTO.class))})
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody final OrderRequestDTO orderRequestDTO) {

        log.info("create new order; orderRequestDTO={}", orderRequestDTO);
        final OrderResponseDTO orderResponseDTO = createOrderUseCase(orderRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }

    @Operation(summary = "Busca pedidos em preparação")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Pedido(s) encontrado(s)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum pedido em preparo encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderResponseDTO>> findOrderInPreparation() {
        log.info("finding orders in preparation");
        final List<OrderResponseDTO> productResponseDTOList =
                listQueuedOrderUseCase.listQueuedOrderUseCase();
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);
    }


    @Operation(summary = "Busca status do pagamento do pedido por Id do pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Pedido encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum pedido encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping(value = "{id}/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderPaymentStatusResponseDTO> findOrderPaymentStatus(@PathVariable("id") final Long id) {
        log.info("finding order payment status, id={}", id);
        final OrderPaymentStatusResponseDTO orderPaymentByOrderId = getOrderPaymentStatusUseCase.getOrderPaymentByOrderId(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderPaymentByOrderId);
    }

    @Operation(summary = "atualiza o status do pagamento")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Pedido atualizado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Nenhum pedido encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Falha ao atualizar o status do pedido",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PatchMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDTO> updatePaymentStatus(
            @PathVariable("id") final Long id,
            @RequestBody UpdateOrderStatusDTO updateOrderStatusDTO) {
        final OrderResponseDTO orderResponseDTO = updateOrderStatusUseCase
                .updateOrderStatus(id, updateOrderStatusDTO.getStatus());
        return ResponseEntity.ok(orderResponseDTO);
    }

    private OrderResponseDTO createOrderUseCase(final OrderRequestDTO orderRequestDTO) {
        if (Objects.isNull(orderRequestDTO.getClientId())) {
            log.info("create new order without client");
            return createOrderUseCase.createOrder(orderRequestDTO.getProductIds());
        }

        log.info("create new order with client; orderRequestDTO.getClientId()={}", orderRequestDTO.getClientId());
        return createOrderUseCase.createOrder(orderRequestDTO.getClientId(), orderRequestDTO.getProductIds());
    }

}
