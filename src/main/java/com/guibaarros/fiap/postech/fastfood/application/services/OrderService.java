package com.guibaarros.fiap.postech.fastfood.application.services;

import com.guibaarros.fiap.postech.fastfood.application.dtos.order.OrderPaymentStatusResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.dtos.order.OrderResponseDTO;
import com.guibaarros.fiap.postech.fastfood.infrastructure.web.httpclient.dto.PaymentServiceResponseDTO;
import com.guibaarros.fiap.postech.fastfood.domain.entities.client.Client;
import com.guibaarros.fiap.postech.fastfood.domain.entities.order.Order;
import com.guibaarros.fiap.postech.fastfood.domain.entities.order.enums.OrderPaymentStatus;
import com.guibaarros.fiap.postech.fastfood.domain.entities.order.enums.OrderStatus;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.Product;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.order.InvalidOrderOperationException;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.order.OrderNotFoundException;
import com.guibaarros.fiap.postech.fastfood.application.usecases.order.ConfirmPaymentUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.order.CreateOrderUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.order.GetOrderPaymentStatusUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.order.ListQueuedOrderUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.order.UpdateOrderStatusUseCase;
import com.guibaarros.fiap.postech.fastfood.domain.repository.order.CountOrderBetweenDatePort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.order.CreatePaymentServiceOrderPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.order.FindOrderByExternalIdPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.order.FindOrderByIdPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.order.FindOrderInPreparationPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.order.SaveOrderPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
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
        GetOrderPaymentStatusUseCase,
        UpdateOrderStatusUseCase {

    private final SaveOrderPort saveOrderPort;
    private final FindOrderByIdPort findOrderByIdPort;
    private final FindOrderByExternalIdPort findOrderByExternalIdPort;
    private final FindOrderInPreparationPort findOrderInPreparationPort;
    private final CountOrderBetweenDatePort countOrderBetweenDatePort;
    private final CreatePaymentServiceOrderPort createPaymentServiceOrderPort;

    private final ClientService clientService;
    private final ProductService productService;

    @Override
    public OrderResponseDTO createOrder(final Long clientId, final List<Long> productIds) {
        final Order order = createOrderWithProducts(productIds);
        final Client client = clientService.findClientById(clientId);
        order.identifyClient(client);
        final Order persistedOrder = createPaymentServiceOrder(saveOrderPort.saveOrder(order));

        log.info("order with client created successfully;");
        return mapEntityToOrderResponseDto(persistedOrder);
    }

    @Override
    public OrderResponseDTO createOrder(final List<Long> productIds) {
        final Order order = createOrderWithProducts(productIds);
        final Order persistedOrder = createPaymentServiceOrder(saveOrderPort.saveOrder(order));
        log.info("order without client created successfully;");
        return mapEntityToOrderResponseDto(persistedOrder);
    }

    @Override
    public List<OrderResponseDTO> listQueuedOrderUseCase() {
        final List<Order> ordersInPreparation = findOrderInPreparationPort
                .findOrderByStatusIn(OrderStatus.getInPreparationStatuses());
        if (ordersInPreparation.isEmpty()) {
            throw new OrderNotFoundException("pedidos em preparo não encontrados");
        }

        final Comparator<Order> compareStatusPresentationOrder = Comparator.comparing(order -> order.getStatus().getPresentationOrder());
        final Comparator<Order> compareCreatedAt = Comparator.comparing(Order::getCreatedAt);

        ordersInPreparation.sort(compareStatusPresentationOrder.thenComparing(compareCreatedAt));

        return ordersInPreparation.stream().map(this::mapEntityToOrderResponseDto).toList();
    }

    @Override
    public void confirmPayment(final Long externalId, final String status) {
        final Optional<Order> optionalOrder = findOrderByExternalIdPort.findOrderByExternalId(externalId);
        if (optionalOrder.isEmpty()) {
            throw new OrderNotFoundException("externalId", externalId.toString());
        }
        try {
            if (!OrderPaymentStatus.valueOf(status).isPaymentApproved()) {
                throw new InvalidOrderOperationException("pagamento não foi aprovado e não pode ser atualizado");
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderOperationException("não existe status de pagamento para o tipo informado: " + status);
        }

        final Order order = optionalOrder.get();

        order.confirmOrderPayment();
        order.sendToPreparation();
        saveOrderPort.saveOrder(order);
        log.info("order payment confirmed; order sent to preparation");
    }

    @Override
    public OrderPaymentStatusResponseDTO getOrderPaymentByOrderId(final Long orderId) {
        final Order order = getOrderById(orderId);
        return mapEntityToOrderPaymentStatusResponseDTO(order);
    }

    @Override
    public OrderResponseDTO updateOrderStatus(final Long id, final String status) {
        try {
            final OrderStatus orderStatus = OrderStatus.valueOf(status);
            final Order order = getOrderById(id);
            switch (orderStatus) {
                case PREPARING -> order.startPreparation();
                case READY -> order.finishPreparation();
                case RECEIVED -> order.deliverToClient();
                case FINISHED -> order.finishOrder();
                case CANCELLED -> order.cancelOrder();
                default -> throw new InvalidOrderOperationException("não é possível atualizar o status para " + status);
            }
            return mapEntityToOrderResponseDto(saveOrderPort.saveOrder(order));
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderOperationException("não existe status para o tipo informado: " + status);
        }
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
        orderResponseDTO.setPaymentQrCodeData(order.getPaymentQrCodeData());
        orderResponseDTO.setExternalId(order.getExternalId());
        return orderResponseDTO;
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

    private Order createPaymentServiceOrder(final Order order) {
        // Integração fake com o MP
        final PaymentServiceResponseDTO paymentServiceOrder =
                createPaymentServiceOrderPort.createPaymentServiceOrder(order.getId(), order.getTotalAmount());
        order.updatePaymentServiceIntegrationData(
                paymentServiceOrder.getQrData(),
                paymentServiceOrder.getExternalId());
        return saveOrderPort.saveOrder(order);
    }
}
