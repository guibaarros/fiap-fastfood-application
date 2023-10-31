package com.guibaarros.fiap.postech.fastfood.adapters.persistence.product;

import com.guibaarros.fiap.postech.fastfood.application.domain.product.Product;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.ProductValueObject;
import com.guibaarros.fiap.postech.fastfood.application.domain.product.enums.ProductCategory;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.product.DeleteProductPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.product.FindProductByCategoryPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.product.FindProductByIdPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.product.SaveProductPort;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.product.ValidateProductValueObjectPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ProductRepository implements
        DeleteProductPort,
        FindProductByCategoryPort,
        SaveProductPort,
        ValidateProductValueObjectPort,
        FindProductByIdPort {

    private final ProductJpaRepository repository;

    @Override
    public void delete(final Product product) {
        repository.delete(product);
    }

    @Override
    public List<Product> findProductByCategory(final ProductCategory productCategory) {
        return repository.findAllByCategory(productCategory);
    }

    @Override
    public Product save(final Product product) {
        return repository.save(product);
    }

    @Override
    public boolean existsProductValueObject(ProductValueObject productValueObject) {
        return repository.existsByNameAndCategoryAndPriceAndDescription(
                productValueObject.name(),
                productValueObject.category(),
                productValueObject.price(),
                productValueObject.description()
        );
    }

    @Override
    public Optional<Product> findProductById(final Long id) {
        return repository.findById(id);
    }
}
