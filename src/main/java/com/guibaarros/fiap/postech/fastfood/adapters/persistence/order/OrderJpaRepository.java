package com.guibaarros.fiap.postech.fastfood.adapters.persistence.order;

import com.guibaarros.fiap.postech.fastfood.application.domain.order.Order;
import com.guibaarros.fiap.postech.fastfood.application.domain.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    List<Order> findOrderByStatusIn(List<OrderStatus> orderStatusList);

    int countByCreatedAtBetween(LocalDateTime initialDate, LocalDateTime finalDate);

    Optional<Order> findOrderByExternalId(Long externalId);
}
