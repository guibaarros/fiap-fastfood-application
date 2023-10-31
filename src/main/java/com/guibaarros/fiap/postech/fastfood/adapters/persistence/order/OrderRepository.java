package com.guibaarros.fiap.postech.fastfood.adapters.persistence.order;

import com.guibaarros.fiap.postech.fastfood.application.domain.order.Order;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.enums.OrderStatus;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.CountOrderBetweenDatePort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.FindOrderByIdPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.FindOrderInPreparationPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.SaveOrderPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class OrderRepository implements
        SaveOrderPort,
        FindOrderByIdPort,
        FindOrderInPreparationPort,
        CountOrderBetweenDatePort {

    private final OrderJpaRepository repository;

    @Override
    public Optional<Order> findOrderById(final Long id) {
        return repository.findById(id);
    }

    @Override
    public Order saveOrder(final Order order) {
        return repository.save(order);
    }

    @Override
    public List<Order> findOrderByStatusIn(final List<OrderStatus> orderStatusList) {
        return repository.findOrderByStatusIn(orderStatusList);
    }

    @Override
    public int countOrderBetweenDate(final LocalDateTime initialDate, final LocalDateTime finalDate) {
        return repository.countByCreatedAtBetween(initialDate, finalDate);
    }
}
