package com.guibaarros.fiap.postech.fastfood.application.domain.order;

import com.guibaarros.fiap.postech.fastfood.application.domain.client.Client;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.Product;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.enums.ProductCategory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {
    @Test
    void identifyClient() {
        final Client client = new Client(
                34980069088L,
                "Guilherme",
                "guilherme@gmail.com"
        );

        final Order order = new Order();
        order.identifyClient(client);

        assertEquals(client, order.getClient());
    }

    @Test
    void addProducts() {
        final Product product = new Product(
                "Água",
                ProductCategory.DRINK,
                BigDecimal.valueOf(4L),
                "Água mineral sem gás",
                null
        );

        final List<Product> products = Collections.singletonList(product);

        final Order order = new Order();
        order.addProducts(products);

        assertEquals(products, order.getProducts());
    }

    @Test
    void generateOrderNumber() {
        Integer expectedNumber = 7;
        Integer orderQuantityToday = 6;

        final Order order = new Order();
        order.generateOrderNumber(orderQuantityToday);

        assertEquals(expectedNumber, order.getNumber());
    }
}