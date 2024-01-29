package com.guibaarros.fiap.postech.fastfood.application.services;

import com.guibaarros.fiap.postech.fastfood.application.dtos.product.ProductResponseDTO;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.Product;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.ProductImage;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.ProductValueObject;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.enums.ProductCategory;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.product.ProductAlreadyExistsException;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.product.ProductNotFoundException;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.CreateProductUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.DeleteProductUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.FindProductByCategoryUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.UpdateProductUseCase;
import com.guibaarros.fiap.postech.fastfood.domain.repository.product.DeleteProductPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.product.FindProductByCategoryPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.product.FindProductByIdPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.product.SaveProductPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.product.ValidateProductValueObjectPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService implements
        CreateProductUseCase,
        DeleteProductUseCase,
        FindProductByCategoryUseCase,
        UpdateProductUseCase {

    private final SaveProductPort saveProductPort;
    private final FindProductByCategoryPort findProductByCategoryPort;
    private final DeleteProductPort deleteProductPort;
    private final FindProductByIdPort findProductByIdPort;
    private final ValidateProductValueObjectPort validateProductValueObjectPort;

    @Override
    public ProductResponseDTO createProduct(final ProductValueObject productValueObject) throws IOException {
        if (validateProductValueObjectPort.existsProductValueObject(productValueObject)) {
            throw new ProductAlreadyExistsException(productValueObject);
        }

        final Product persistedProduct = saveProductPort.save(mapValueObjectToEntity(productValueObject));

        log.info("product created successfully;");
        return mapEntityToResponseDto(persistedProduct);
    }

    @Override
    public void delete(final Long id) {
        final Product product = findProductById(id);
        deleteProductPort.delete(product);
        log.info("product deleted successfully;");
    }

    @Override
    public ProductResponseDTO update(final Long id, final ProductValueObject productValueObject) throws IOException {
        final Product product = findProductById(id);
        product.update(productValueObject);

        if (Objects.nonNull(productValueObject.image())) {
            final ProductImage productImage = getProductImage(productValueObject);
            product.updateImage(productImage);
        }

        final Product updatedProduct = saveProductPort.save(product);
        log.info("product updated successfully;");
        return mapEntityToResponseDto(updatedProduct);
    }

    @Override
    public List<ProductResponseDTO> findProductByCategory(final ProductCategory productCategory) {
        final List<Product> productListByCategory = findProductByCategoryPort
                .findProductByCategory(productCategory);
        if (productListByCategory.isEmpty()) {
            throw new ProductNotFoundException(productCategory);
        }

        return productListByCategory
                .stream()
                .map(this::mapEntityToResponseDto)
                .toList();
    }

    private Product mapValueObjectToEntity(final ProductValueObject productValueObject) throws IOException {
        return new Product(
                productValueObject.name(),
                productValueObject.category(),
                productValueObject.price(),
                productValueObject.description(),
                Objects.nonNull(productValueObject.image()) ? getProductImage(productValueObject) : null
        );
    }

    protected  Product findProductById(final Long id) {
        final Optional<Product> optProductById = findProductByIdPort.findProductById(id);
        if (optProductById.isPresent()) {
            return optProductById.get();
        }
        throw new ProductNotFoundException(id);
    }

    protected ProductResponseDTO mapEntityToResponseDto(final Product product) {
        final ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(product.getId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setCategory(product.getCategory());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setCreatedAt(product.getCreatedAt());
        return productResponseDTO;
    }

    private ProductImage getProductImage(final ProductValueObject productValueObject) throws IOException {
        return new ProductImage(
                productValueObject.image().getBytes(),
                productValueObject.image().getName(),
                productValueObject.image().getContentType(),
                productValueObject.image().getSize()
        );
    }
}
