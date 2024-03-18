package com.guibaarros.fiap.postech.fastfood.adapters.controller;

import com.guibaarros.fiap.postech.fastfood.application.dtos.order.OrderRequestDTO;
import com.guibaarros.fiap.postech.fastfood.application.dtos.order.OrderResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.usecases.order.ConfirmPaymentUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.order.ListQueuedOrderUseCase;
import com.guibaarros.fiap.postech.fastfood.interfaces.order.OrderController;
import com.guibaarros.fiap.postech.fastfood.restcontroller.OrderRestController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {

    @Mock
    private ListQueuedOrderUseCase listQueuedOrderUseCase;

    @Mock
    private ConfirmPaymentUseCase confirmPaymentUseCase;

    @Mock
    private OrderController orderController;

    @InjectMocks
    private OrderRestController orderRestController;

    @Test
    void createOrder() {
        final Long clientId = 1L;
        final List<Long> productList = Collections.singletonList(1L);

        final Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer token.example.signature");

        final OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setClientId(clientId);
        orderRequestDTO.setProductIds(productList);

        final OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setFormattedNumber("001");
        orderResponseDTO.setTotalAmount(BigDecimal.valueOf(3L));


        Mockito.when(orderController.createOrder(any(), any())).thenReturn(orderResponseDTO);

        final ResponseEntity<OrderResponseDTO> expectedResponseEntity =
                ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);

        final ResponseEntity<OrderResponseDTO> actualResponseEntity = orderRestController.createOrder(headers, orderRequestDTO);

        Assertions.assertEquals(expectedResponseEntity, actualResponseEntity);
        Mockito.verify(orderController, Mockito.times(1)).createOrder(any(), any());
    }

    @Test
    void createOrderWithoutClient() {
        final List<Long> productList = Collections.singletonList(1L);
        final Map<String, String> headers = new HashMap<>();

        final OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setProductIds(productList);

        final OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setFormattedNumber("001");
        orderResponseDTO.setTotalAmount(BigDecimal.valueOf(3L));

        Mockito.when(orderController.createOrder(any(), any())).thenReturn(orderResponseDTO);

        final ResponseEntity<OrderResponseDTO> expectedResponseEntity =
                ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);

        final ResponseEntity<OrderResponseDTO> actualResponseEntity = orderRestController.createOrder(headers, orderRequestDTO);

        Assertions.assertEquals(expectedResponseEntity, actualResponseEntity);
        Mockito.verify(orderController, Mockito.times(1)).createOrder(any(), any());
    }

    @Test
    void findOrderInPreparation() {
        final OrderResponseDTO expectedResponseDto = new OrderResponseDTO();
        expectedResponseDto.setFormattedNumber("001");
        expectedResponseDto.setTotalAmount(BigDecimal.valueOf(3L));

        final List<OrderResponseDTO> orderResponseDTOList = Collections.singletonList(expectedResponseDto);

        final ResponseEntity<Object> expectedResponseEntity =
                ResponseEntity.status(HttpStatus.OK).body(orderResponseDTOList);

        Mockito.when(listQueuedOrderUseCase.listQueuedOrderUseCase()).thenReturn(orderResponseDTOList);

        final ResponseEntity<List<OrderResponseDTO>> actualResponseEntity = orderRestController.findOrderInPreparation();

        Assertions.assertEquals(expectedResponseEntity, actualResponseEntity);
    }
}