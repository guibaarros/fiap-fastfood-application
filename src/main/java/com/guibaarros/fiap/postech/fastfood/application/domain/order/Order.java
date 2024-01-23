package com.guibaarros.fiap.postech.fastfood.application.domain.order;

import com.guibaarros.fiap.postech.fastfood.application.domain.client.Client;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.enums.OrderPaymentStatus;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.enums.OrderStatus;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.Product;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.order.InvalidOrderOperationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Entity(name = "tb_order")
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column
    private Integer number;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToMany
    @JoinTable(
            name = "tb_order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @Column
    private BigDecimal totalAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderPaymentStatus paymentStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime finishedAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime paymentStatusUpdatedAt;

    @Column
    private String paymentQrCodeData;

    @Column
    private Long externalId;

    @PrePersist
    private void prePersist() {
        this.status = OrderStatus.AWAITING_PAYMENT;
        this.paymentStatus = OrderPaymentStatus.AWAITING_PAYMENT;
        this.createdAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    public void identifyClient(final Client client) {
        this.client = client;
    }

    public void addProducts(final List<Product> products) {
        this.products = products;
        this.totalAmount = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void generateOrderNumber(final Integer orderQuantityToday) {
        this.number = orderQuantityToday + 1;
    }

    public void confirmOrderPayment() {
        if (OrderPaymentStatus.AWAITING_PAYMENT.equals(this.paymentStatus)) {
            this.status = OrderStatus.AWAITING_PREPARATION;
        }
        this.paymentStatus = OrderPaymentStatus.PAID;
        this.paymentStatusUpdatedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    public void sendToPreparation() {
        if (!OrderPaymentStatus.PAID.equals(this.paymentStatus)) {
            throw new InvalidOrderOperationException("o pedido deve estar pago antes de ser enviado para preparo");
        }

        this.status = OrderStatus.AWAITING_PREPARATION;
    }

    public void startPreparation() {
        if (!OrderStatus.AWAITING_PREPARATION.equals(this.status)) {
            throw new InvalidOrderOperationException(
                    "o pedido deve estar aguardando preparo antes de iniciar o preparo");
        }
        this.status = OrderStatus.PREPARING;
    }

    public void finishPreparation() {
        if (!OrderStatus.PREPARING.equals(this.status)) {
            throw new InvalidOrderOperationException(
                    "o pedido deve estar sendo preparado antes de ser finalizado");
        }
        this.status = OrderStatus.READY;
    }

    public void deliverToClient() {
        if (!OrderStatus.READY.equals(this.status)) {
            throw new InvalidOrderOperationException(
                    "o pedido deve estar pronto antes de ser entregue ao cliente");
        }
        this.status = OrderStatus.RECEIVED;
    }

    public void finishOrder() {
        if (!OrderStatus.RECEIVED.equals(this.status)) {
            throw new InvalidOrderOperationException(
                    "o pedido deve ter sido retirado pelo cliente antes de ser finalizado");
        }
        this.status = OrderStatus.FINISHED;
        this.finishedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    public void cancelOrder() {
        if (OrderStatus.FINISHED.equals(this.status) || OrderStatus.RECEIVED.equals(this.status)) {
            throw new InvalidOrderOperationException(
                    "o pedido finalizado ou retirado pelo cliente não pode ser cancelado");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public void cancelPayment() {
        this.paymentStatus = OrderPaymentStatus.CANCELLED;
        this.paymentStatusUpdatedAt = LocalDateTime.now();
    }

    public Long getTotalWaitingTimeInMinutes() {
        return getTotalWaitingTime().toMinutes();
    }

    public void updatePaymentServiceIntegrationData(final String qrCodeData, final Long externalId) {
        this.paymentQrCodeData = qrCodeData;
        this.externalId = externalId;
    }

    private Duration getTotalWaitingTime() {
        if (OrderStatus.FINISHED.equals(this.status)) {
            return Duration.between(this.createdAt, this.finishedAt);
        }

        if (Objects.isNull(this.createdAt)) {
            return Duration.ZERO;
        }

        return Duration.between(this.createdAt, LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
    }
}
