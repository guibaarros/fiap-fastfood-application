package com.guibaarros.fiap.postech.fastfood.adapters.persistence.product;

import com.guibaarros.fiap.postech.fastfood.application.domain.product.Product;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.enums.ProductCategory;
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
