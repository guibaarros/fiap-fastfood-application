package com.guibaarros.fiap.postech.fastfood.application.services;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.client.ClientResponseDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderResponseDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.product.ProductResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.domain.client.Client;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.Order;
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

        Mockito.when(productService.findProductById(1L)).thenReturn(product);
        Mockito.when((countOrderBetweenDatePort.countOrderBetweenDate(Mockito.any(), Mockito.any())))
                .thenReturn(orderQuantity);
        Mockito.when(clientService.findClientById(1L)).thenReturn(client);
        Mockito.when(saveOrderPort.saveOrder(Mockito.any())).thenReturn(persistedOrder);
        Mockito.when(clientService.mapEntityToResponseDto(Mockito.any())).thenReturn(clientResponseDTO);
        Mockito.when(productService.mapEntityToResponseDto(Mockito.any())).thenReturn(productResponseDTO);

        final OrderResponseDTO actualOrderResponseDTO =
                orderService.createOrder(1L, Collections.singletonList(1L));

        Assertions.assertEquals(expectedResponseDto.getFormattedNumber(), actualOrderResponseDTO.getFormattedNumber());
        Assertions.assertEquals(expectedResponseDto.getTotalAmount(), actualOrderResponseDTO.getTotalAmount());
        Assertions.assertNotNull(actualOrderResponseDTO.getClient());
        Assertions.assertNotNull(actualOrderResponseDTO.getProducts());
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

        Mockito.when(productService.findProductById(1L)).thenReturn(product);
        Mockito.when((countOrderBetweenDatePort.countOrderBetweenDate(Mockito.any(), Mockito.any())))
                .thenReturn(orderQuantity);
        Mockito.when(saveOrderPort.saveOrder(Mockito.any())).thenReturn(persistedOrder);
        Mockito.when(productService.mapEntityToResponseDto(Mockito.any())).thenReturn(productResponseDTO);

        final OrderResponseDTO actualOrderResponseDTO =
                orderService.createOrder(Collections.singletonList(1L));

        Assertions.assertEquals(expectedResponseDto.getFormattedNumber(), actualOrderResponseDTO.getFormattedNumber());
        Assertions.assertEquals(expectedResponseDto.getTotalAmount(), actualOrderResponseDTO.getTotalAmount());
        Assertions.assertNull(actualOrderResponseDTO.getClient());
        Assertions.assertNotNull(actualOrderResponseDTO.getProducts());
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

        Mockito.when(clientService.mapEntityToResponseDto(Mockito.any())).thenReturn(clientResponseDTO);
        Mockito.when(productService.mapEntityToResponseDto(Mockito.any())).thenReturn(productResponseDTO);
        Mockito.when(findOrderInPreparationPort.findOrderByStatusIn(Mockito.anyList()))
                .thenReturn(Collections.singletonList(persistedOrder));

        final List<OrderResponseDTO> actualOrderResponseDTOList = orderService.listQueuedOrderUseCase();
        final OrderResponseDTO actualOrderResponseDTO = actualOrderResponseDTOList.get(0);

        Assertions.assertEquals(expectedResponseDto.getFormattedNumber(), actualOrderResponseDTO.getFormattedNumber());
        Assertions.assertEquals(expectedResponseDto.getTotalAmount(), actualOrderResponseDTO.getTotalAmount());
        Assertions.assertNotNull(actualOrderResponseDTO.getClient());
        Assertions.assertNotNull(actualOrderResponseDTO.getProducts());
    }

    @Test
    void listQueuedOrderUseCaseNotFound() {
        Mockito.when(findOrderInPreparationPort.findOrderByStatusIn(Mockito.anyList()))
                .thenReturn(Collections.emptyList());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.listQueuedOrderUseCase());
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

        Mockito.when(findOrderByIdPort.findOrderById(Mockito.eq(orderId))).thenReturn(Optional.of(order));
        Mockito.when(saveOrderPort.saveOrder(Mockito.any())).thenReturn(order);

        orderService.confirmPayment(orderId);

        Mockito.verify(findOrderByIdPort, Mockito.times(1)).findOrderById(orderId);
        Mockito.verify(saveOrderPort, Mockito.times(1)).saveOrder(Mockito.any());
    }

    @Test
    void confirmPaymentNotFound() {
        final Long orderId = 1L;

        Mockito.when(findOrderByIdPort.findOrderById(Mockito.eq(orderId))).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.confirmPayment(orderId));
    }
}