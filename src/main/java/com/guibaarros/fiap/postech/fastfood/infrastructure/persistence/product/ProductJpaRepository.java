package com.guibaarros.fiap.postech.fastfood.infrastructure.persistence.product;

import com.guibaarros.fiap.postech.fastfood.domain.entities.product.Product;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.enums.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory(ProductCategory productCategory);

    boolean existsByNameAndCategoryAndPriceAndDescription(
            String name,
            ProductCategory category,
            BigDecimal price,
            String description
    );
}
