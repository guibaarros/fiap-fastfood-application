package com.guibaarros.fiap.postech.fastfood.domain.repository.order;

import com.guibaarros.fiap.postech.fastfood.domain.entities.order.Order;

import java.util.Optional;

public interface FindOrderByExternalIdPort {

    Optional<Order> findOrderByExternalId(Long externalId);
}
