package com.guibaarros.fiap.postech.fastfood.application.services;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.client.ClientResponseDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderPaymentStatusResponseDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderResponseDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.product.ProductResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.domain.client.Client;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.Order;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.enums.OrderPaymentStatus;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.Product;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.enums.ProductCategory;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.order.OrderNotFoundException;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.CountOrderBetweenDatePort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.FindOrderByIdPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.FindOrderInPreparationPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.SaveOrderPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private SaveOrderPort saveOrderPort;

    @Mock
    private FindOrderByIdPort findOrderByIdPort;

    @Mock
    private FindOrderInPreparationPort findOrderInPreparationPort;

    @Mock
    private CountOrderBetweenDatePort countOrderBetweenDatePort;

    @Mock
    private ClientService clientService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder() {
        final Long cpf = 13869555076L;
        final String clientName = "Guilherme";
        final String email = "guilherme@gmail.com";

        final Client client = new Client(
                cpf,
                clientName,
                email
        );

        final ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setCpf(cpf);
        clientResponseDTO.setName(clientName);
        clientResponseDTO.setEmail(email);

        final String productName = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final Product product = new Product(
                productName,
                category,
                price,
                description,
                null);

        final int orderQuantity = 0;

        final ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setName(productName);
        productResponseDTO.setCategory(category);
        productResponseDTO.setPrice(price);
        productResponseDTO.setDescription(description);

        final Order persistedOrder = new Order();
        persistedOrder.addProducts(Collections.singletonList(product));
        persistedOrder.identifyClient(client);
        persistedOrder.generateOrderNumber(orderQuantity);

        final OrderResponseDTO expectedResponseDto = new OrderResponseDTO();
        expectedResponseDto.setFormattedNumber("001");
        expectedResponseDto.setTotalAmount(BigDecimal.valueOf(3L));

        when(productService.findProductById(1L)).thenReturn(product);
        when((countOrderBetweenDatePort.countOrderBetweenDate(any(), any())))
                .thenReturn(orderQuantity);
        when(clientService.findClientById(1L)).thenReturn(client);
        when(saveOrderPort.saveOrder(any())).thenReturn(persistedOrder);
        when(clientService.mapEntityToResponseDto(any())).thenReturn(clientResponseDTO);
        when(productService.mapEntityToResponseDto(any())).thenReturn(productResponseDTO);

        final OrderResponseDTO actualOrderResponseDTO =
                orderService.createOrder(1L, Collections.singletonList(1L));

        assertEquals(expectedResponseDto.getFormattedNumber(), actualOrderResponseDTO.getFormattedNumber());
        assertEquals(expectedResponseDto.getTotalAmount(), actualOrderResponseDTO.getTotalAmount());
        assertNotNull(actualOrderResponseDTO.getClient());
        assertNotNull(actualOrderResponseDTO.getProducts());
    }

    @Test
    void createOrderWithoutClient() {
        final String productName = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final Product product = new Product(
                productName,
                category,
                price,
                description,
                null);

        final int orderQuantity = 0;

        final ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setName(productName);
        productResponseDTO.setCategory(category);
        productResponseDTO.setPrice(price);
        productResponseDTO.setDescription(description);

        final Order persistedOrder = new Order();
        persistedOrder.addProducts(Collections.singletonList(product));
        persistedOrder.generateOrderNumber(orderQuantity);

        final OrderResponseDTO expectedResponseDto = new OrderResponseDTO();
        expectedResponseDto.setFormattedNumber("001");
        expectedResponseDto.setTotalAmount(BigDecimal.valueOf(3L));

        when(productService.findProductById(1L)).thenReturn(product);
        when((countOrderBetweenDatePort.countOrderBetweenDate(any(), any())))
                .thenReturn(orderQuantity);
        when(saveOrderPort.saveOrder(any())).thenReturn(persistedOrder);
        when(productService.mapEntityToResponseDto(any())).thenReturn(productResponseDTO);

        final OrderResponseDTO actualOrderResponseDTO =
                orderService.createOrder(Collections.singletonList(1L));

        assertEquals(expectedResponseDto.getFormattedNumber(), actualOrderResponseDTO.getFormattedNumber());
        assertEquals(expectedResponseDto.getTotalAmount(), actualOrderResponseDTO.getTotalAmount());
        assertNull(actualOrderResponseDTO.getClient());
        assertNotNull(actualOrderResponseDTO.getProducts());
    }

    @Test
    void listQueuedOrderUseCase() {
        final Long cpf = 13869555076L;
        final String clientName = "Guilherme";
        final String email = "guilherme@gmail.com";

        final Client client = new Client(
                cpf,
                clientName,
                email
        );

        final ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setCpf(cpf);
        clientResponseDTO.setName(clientName);
        clientResponseDTO.setEmail(email);

        final String productName = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final Product product = new Product(
                productName,
                category,
                price,
                description,
                null);

        final int orderQuantity = 0;

        final ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setName(productName);
        productResponseDTO.setCategory(category);
        productResponseDTO.setPrice(price);
        productResponseDTO.setDescription(description);

        final Order persistedOrder = new Order();
        persistedOrder.addProducts(Collections.singletonList(product));
        persistedOrder.identifyClient(client);
        persistedOrder.generateOrderNumber(orderQuantity);

        final OrderResponseDTO expectedResponseDto = new OrderResponseDTO();
        expectedResponseDto.setFormattedNumber("001");
        expectedResponseDto.setTotalAmount(BigDecimal.valueOf(3L));

        when(clientService.mapEntityToResponseDto(any())).thenReturn(clientResponseDTO);
        when(productService.mapEntityToResponseDto(any())).thenReturn(productResponseDTO);
        when(findOrderInPreparationPort.findOrderByStatusIn(Mockito.anyList()))
                .thenReturn(Collections.singletonList(persistedOrder));

        final List<OrderResponseDTO> actualOrderResponseDTOList = orderService.listQueuedOrderUseCase();
        final OrderResponseDTO actualOrderResponseDTO = actualOrderResponseDTOList.get(0);

        assertEquals(expectedResponseDto.getFormattedNumber(), actualOrderResponseDTO.getFormattedNumber());
        assertEquals(expectedResponseDto.getTotalAmount(), actualOrderResponseDTO.getTotalAmount());
        assertNotNull(actualOrderResponseDTO.getClient());
        assertNotNull(actualOrderResponseDTO.getProducts());
    }

    @Test
    void listQueuedOrderUseCaseNotFound() {
        when(findOrderInPreparationPort.findOrderByStatusIn(Mockito.anyList()))
                .thenReturn(Collections.emptyList());

        assertThrows(OrderNotFoundException.class, () -> orderService.listQueuedOrderUseCase());
    }

    @Test
    void confirmPayment() {
        final Long cpf = 13869555076L;
        final String clientName = "Guilherme";
        final String email = "guilherme@gmail.com";

        final Client client = new Client(
                cpf,
                clientName,
                email
        );

        final String productName = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final Product product = new Product(
                productName,
                category,
                price,
                description,
                null);

        final Long orderId = 1L;

        final Order order = new Order();
        order.addProducts(Collections.singletonList(product));
        order.identifyClient(client);

        when(findOrderByIdPort.findOrderById(eq(orderId))).thenReturn(Optional.of(order));
        when(saveOrderPort.saveOrder(any())).thenReturn(order);

        orderService.confirmPayment(orderId);

        verify(findOrderByIdPort, times(1)).findOrderById(orderId);
        verify(saveOrderPort, times(1)).saveOrder(any());
    }

    @Test
    void confirmPaymentNotFound() {
        final Long orderId = 1L;

        when(findOrderByIdPort.findOrderById(eq(orderId))).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.confirmPayment(orderId));
    }

    @Test
    void getPaidOrderPaymentByOrderId() {
        final Long orderId = 1L;
        final String productName = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final Product product = new Product(
                productName,
                category,
                price,
                description,
                null);

        final Order order = new Order();
        order.addProducts(Collections.singletonList(product));
        order.confirmOrderPayment();

        final OrderPaymentStatusResponseDTO expectedDto = new OrderPaymentStatusResponseDTO();
        expectedDto.setId(order.getId());
        expectedDto.setPaymentStatus(OrderPaymentStatus.PAID);
        expectedDto.setIsPaymentApproved(OrderPaymentStatus.PAID.isPaymentApproved());

        when(findOrderByIdPort.findOrderById(eq(orderId))).thenReturn(Optional.of(order));

        final OrderPaymentStatusResponseDTO orderPaymentStatusResponseDTO =
                orderService.getOrderPaymentByOrderId(orderId);

        assertEquals(expectedDto.getPaymentStatus(), orderPaymentStatusResponseDTO.getPaymentStatus());
        assertEquals(expectedDto.getIsPaymentApproved(), orderPaymentStatusResponseDTO.getIsPaymentApproved());
        assertNotNull(orderPaymentStatusResponseDTO.getPaymentStatusUpdatedAt());
    }
}