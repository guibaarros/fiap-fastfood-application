package com.guibaarros.fiap.postech.fastfood.adapters.controller;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderRequestDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.order.ConfirmPaymentUseCase;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.order.CreateOrderUseCase;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.order.ListQueuedOrderUseCase;
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
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private ListQueuedOrderUseCase listQueuedOrderUseCase;

    @Mock
    private ConfirmPaymentUseCase confirmPaymentUseCase;

    @InjectMocks
    private OrderController orderController;

    @Test
    void createOrder() {
        final Long clientId = 1L;
        final List<Long> productList = Collections.singletonList(1L);

        final OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setClientId(clientId);
        orderRequestDTO.setProductIds(productList);

        final OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setFormattedNumber("001");
        orderResponseDTO.setTotalAmount(BigDecimal.valueOf(3L));

        Mockito.when(createOrderUseCase.createOrder(
                        Mockito.eq(orderRequestDTO.getClientId()),
                        Mockito.eq(orderRequestDTO.getProductIds())
                )).thenReturn(orderResponseDTO);

        final ResponseEntity<OrderResponseDTO> expectedResponseEntity =
                ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);

        final ResponseEntity<OrderResponseDTO> actualResponseEntity = orderController.createOrder(orderRequestDTO);

        Assertions.assertEquals(expectedResponseEntity, actualResponseEntity);
        Mockito.verify(createOrderUseCase, Mockito.times(0)).createOrder(Mockito.any());
    }

    @Test
    void createOrderWithoutClient() {
        final List<Long> productList = Collections.singletonList(1L);

        final OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setProductIds(productList);

        final OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setFormattedNumber("001");
        orderResponseDTO.setTotalAmount(BigDecimal.valueOf(3L));

        Mockito.when(createOrderUseCase.createOrder(
                Mockito.eq(orderRequestDTO.getProductIds())
        )).thenReturn(orderResponseDTO);

        final ResponseEntity<OrderResponseDTO> expectedResponseEntity =
                ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);

        final ResponseEntity<OrderResponseDTO> actualResponseEntity = orderController.createOrder(orderRequestDTO);

        Assertions.assertEquals(expectedResponseEntity, actualResponseEntity);
        Mockito.verify(createOrderUseCase, Mockito.times(0))
                .createOrder(Mockito.any(), Mockito.any());
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

        final ResponseEntity<List<OrderResponseDTO>> actualResponseEntity = orderController.findOrderInPreparation();

        Assertions.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

//    @Test
//    void confirmOrderPayment() {
//        final Long id = 1L;
//
//        final ResponseEntity<Void> expectedResponseEntity = ResponseEntity.status(HttpStatus.OK).build();
//
//        Mockito.doNothing()
//                .when(confirmPaymentUseCase).confirmPayment(Mockito.eq(id));
//
//        final ResponseEntity<Void> actualResponseEntity = orderController.confirmOrderPayment(id);
//
//        Assertions.assertEquals(expectedResponseEntity, actualResponseEntity);
//        Mockito.verify(confirmPaymentUseCase, Mockito.times(1))
//                .confirmPayment(Mockito.eq(id));
//    }
}