package com.guibaarros.fiap.postech.fastfood.application.services;

import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderPaymentStatusResponseDTO;
import com.guibaarros.fiap.postech.fastfood.adapters.dtos.order.OrderResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.domain.client.Client;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.Order;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.enums.OrderStatus;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.Product;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.order.OrderNotFoundException;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.order.ConfirmPaymentUseCase;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.order.CreateOrderUseCase;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.order.GetOrderPaymentStatusUseCase;
import com.guibaarros.fiap.postech.fastfood.application.port.incoming.order.ListQueuedOrderUseCase;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.CountOrderBetweenDatePort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.FindOrderByIdPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.FindOrderInPreparationPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.SaveOrderPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService implements
        CreateOrderUseCase,
        ListQueuedOrderUseCase,
        ConfirmPaymentUseCase,
        GetOrderPaymentStatusUseCase {

    private final SaveOrderPort saveOrderPort;
    private final FindOrderByIdPort findOrderByIdPort;
    private final FindOrderInPreparationPort findOrderInPreparationPort;
    private final CountOrderBetweenDatePort countOrderBetweenDatePort;

    private final ClientService clientService;
    private final ProductService productService;

    @Override
    public OrderResponseDTO createOrder(final Long clientId, final List<Long> productIds) {
        final Order order = createOrderWithProducts(productIds);
        final Client client = clientService.findClientById(clientId);
        order.identifyClient(client);
        final Order persistedOrder = saveOrderPort.saveOrder(order);
        log.info("order with client created successfully;");
        return mapEntityToOrderResponseDto(persistedOrder);
    }

    @Override
    public OrderResponseDTO createOrder(final List<Long> productIds) {
        final Order order = createOrderWithProducts(productIds);
        final Order persistedOrder = saveOrderPort.saveOrder(order);
        log.info("order without client created successfully;");
        return mapEntityToOrderResponseDto(persistedOrder);
    }

    private Order createOrderWithProducts(final List<Long> productIds) {
        final List<Product> products = productIds.stream().map(productService::findProductById).toList();

        final int orderQuantityToday = countOrderBetweenDatePort.countOrderBetweenDate(
                LocalDate.now().atTime(LocalTime.MIN),
                LocalDate.now().atTime(LocalTime.MAX)
        );

        final Order order = new Order();
        order.addProducts(products);
        order.generateOrderNumber(orderQuantityToday);
        return order;
    }

    @Override
    public List<OrderResponseDTO> listQueuedOrderUseCase() {
        final List<Order> ordersInPreparation = findOrderInPreparationPort.findOrderByStatusIn(OrderStatus.getInPreparationStatuses());
        if (ordersInPreparation.isEmpty()) {
            throw new OrderNotFoundException("pedidos em preparo não encontrados");
        }
        return ordersInPreparation.stream().map(this::mapEntityToOrderResponseDto).toList();
    }

    @Override
    public void confirmPayment(final Long id) {
        final Order order = getOrderById(id);

        order.confirmOrderPayment();
        order.sendToPreparation();
        saveOrderPort.saveOrder(order);
        log.info("order payment confirmed; order sent to preparation");
    }

    private OrderResponseDTO mapEntityToOrderResponseDto(final Order order) {
        final OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setPaymentStatus(order.getPaymentStatus());
        orderResponseDTO.setStatus(order.getStatus());
        if (Objects.nonNull(order.getClient())) {
            orderResponseDTO.setClient(clientService.mapEntityToResponseDto(order.getClient()));
        }
        orderResponseDTO.setProducts(
                order.getProducts().stream().map(productService::mapEntityToResponseDto).toList());
        orderResponseDTO.setCreatedAt(order.getCreatedAt());
        orderResponseDTO.setTotalAmount(order.getTotalAmount());
        orderResponseDTO.setUpdatedAt(order.getUpdatedAt());
        orderResponseDTO.setFinishedAt(order.getFinishedAt());
        orderResponseDTO.setWaitingTimeInMinutes(order.getTotalWaitingTimeInMinutes());
        orderResponseDTO.setFormattedNumber(String.format("%03d", order.getNumber()));
        return orderResponseDTO;
    }

    @Override
    public OrderPaymentStatusResponseDTO getOrderPaymentByOrderId(final Long orderId) {
        final Order order = getOrderById(orderId);
        return mapEntityToOrderPaymentStatusResponseDTO(order);
    }

    private Order getOrderById(Long id) {
        final Optional<Order> optOrder = findOrderByIdPort.findOrderById(id);
        if (optOrder.isEmpty()) {
            throw new OrderNotFoundException(id);
        }
        return optOrder.get();
    }

    private OrderPaymentStatusResponseDTO mapEntityToOrderPaymentStatusResponseDTO(final Order order) {
        final OrderPaymentStatusResponseDTO orderPaymentStatusResponseDTO = new OrderPaymentStatusResponseDTO();
        orderPaymentStatusResponseDTO.setId(order.getId());
        orderPaymentStatusResponseDTO.setPaymentStatus(order.getPaymentStatus());
        orderPaymentStatusResponseDTO.setPaymentStatusUpdatedAt(order.getPaymentStatusUpdatedAt());
        orderPaymentStatusResponseDTO.setIsPaymentApproved(order.getPaymentStatus().isPaymentApproved());
        return orderPaymentStatusResponseDTO;
    }
}
