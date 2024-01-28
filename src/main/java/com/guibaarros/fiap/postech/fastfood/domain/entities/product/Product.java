package com.guibaarros.fiap.postech.fastfood.domain.entities.product;

import com.guibaarros.fiap.postech.fastfood.domain.entities.product.enums.ProductCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_product")
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column
    private BigDecimal price;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<ProductImage> image;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    public Product(final String name,
                   final ProductCategory category,
                   final BigDecimal price,
                   final String description,
                   final ProductImage image) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.image = new ArrayList<>();
        this.image.add(image);
    }

    public void update(final ProductValueObject productValueObject) throws IOException {
        this.name = productValueObject.name();
        this.category = productValueObject.category();
        this.price = productValueObject.price();
        this.description = productValueObject.description();
    }

    public void updateImage(final ProductImage image) throws IOException {
        this.image = new ArrayList<>();
        this.image.add(image);
    }
}
