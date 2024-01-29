package com.guibaarros.fiap.postech.fastfood.domain.entities.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity(name = "tb_product_image")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Long id;

    @Lob
    private byte[] image;

    @Column
    private String name;

    @Column
    private String contentType;

    @Column
    private Long size;

    public ProductImage(final byte[] image,
                        final String name,
                        final String contentType,
                        final Long size) {
        this.image = image;
        this.name = name;
        this.contentType = contentType;
        this.size = size;
    }
}
