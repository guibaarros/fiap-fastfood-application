package com.guibaarros.fiap.postech.fastfood.application.services;

import com.guibaarros.fiap.postech.fastfood.application.dtos.product.ProductResponseDTO;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.Product;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.ProductValueObject;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.enums.ProductCategory;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.product.ProductAlreadyExistsException;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.product.ProductNotFoundException;
import com.guibaarros.fiap.postech.fastfood.domain.repository.product.DeleteProductPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.product.FindProductByCategoryPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.product.FindProductByIdPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.product.SaveProductPort;
import com.guibaarros.fiap.postech.fastfood.domain.repository.product.ValidateProductValueObjectPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private SaveProductPort saveProductPort;

    @Mock
    private FindProductByCategoryPort findProductByCategoryPort;

    @Mock
    private DeleteProductPort deleteProductPort;

    @Mock
    private FindProductByIdPort findProductByIdPort;

    @Mock
    private ValidateProductValueObjectPort validateProductValueObjectPort;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct() throws IOException {
        final String name = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final ProductValueObject productValueObject = new ProductValueObject(
                name,
                category,
                price,
                description,
                null
        );

        final Product persistedProduct = new Product(
                name,
                category,
                price,
                description,
                null);

        final ProductResponseDTO expectedProductResponseDTO = new ProductResponseDTO();
        expectedProductResponseDTO.setName(name);
        expectedProductResponseDTO.setCategory(category);
        expectedProductResponseDTO.setPrice(price);
        expectedProductResponseDTO.setDescription(description);

        Mockito.when(validateProductValueObjectPort.existsProductValueObject(Mockito.eq(productValueObject)))
                .thenReturn(Boolean.FALSE);
        Mockito.when(saveProductPort.save(Mockito.any())).thenReturn(persistedProduct);

        final ProductResponseDTO actualProductResponseDTO = productService.createProduct(productValueObject);

        Assertions.assertEquals(expectedProductResponseDTO.getName(), actualProductResponseDTO.getName());
        Assertions.assertEquals(expectedProductResponseDTO.getCategory(), actualProductResponseDTO.getCategory());
        Assertions.assertEquals(expectedProductResponseDTO.getPrice(), actualProductResponseDTO.getPrice());
        Assertions.assertEquals(expectedProductResponseDTO.getDescription(), actualProductResponseDTO.getDescription());
    }

    @Test
    void createProductAlreadyExistsByValueObject() {
        final String name = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final ProductValueObject productValueObject = new ProductValueObject(
                name,
                category,
                price,
                description,
                null
        );

        Mockito.when(validateProductValueObjectPort.existsProductValueObject(Mockito.eq(productValueObject)))
                .thenReturn(Boolean.TRUE);

        Assertions.assertThrows(ProductAlreadyExistsException.class,
                () -> productService.createProduct(productValueObject));
    }

    @Test
    void delete() {
        final Long id = 1L;
        final String name = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final Product persistedProduct = new Product(
                name,
                category,
                price,
                description,
                null);

        Mockito.when(findProductByIdPort.findProductById(Mockito.eq(id))).thenReturn(Optional.of(persistedProduct));

        productService.delete(id);

        Mockito.verify(deleteProductPort, Mockito.times(1)).delete(persistedProduct);
    }

    @Test
    void deleteNotFound() {
        final Long id = 1L;

        Mockito.when(findProductByIdPort.findProductById(Mockito.eq(id))).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.delete(id));
    }

    @Test
    void update() throws IOException {
        final Long id = 1L;
        final String name = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final ProductValueObject productValueObject = new ProductValueObject(
                name,
                category,
                price,
                description,
                null
        );

        final Product persistedProduct = new Product(
                name,
                category,
                price,
                description,
                null);

        final String updatedName = "Fritas";
        final ProductCategory updatedCategory = ProductCategory.SIDE;
        final BigDecimal updatedPrice = BigDecimal.valueOf(8L);
        final String updatedDescription = "Batata Frita";

        final Product updatedProduct = new Product(
                updatedName,
                updatedCategory,
                updatedPrice,
                updatedDescription,
                null);

        final ProductResponseDTO expectedProductResponseDTO = new ProductResponseDTO();
        expectedProductResponseDTO.setName(updatedName);
        expectedProductResponseDTO.setCategory(updatedCategory);
        expectedProductResponseDTO.setPrice(updatedPrice);
        expectedProductResponseDTO.setDescription(updatedDescription);

        Mockito.when(findProductByIdPort.findProductById(Mockito.eq(id))).thenReturn(Optional.of(persistedProduct));
        Mockito.when(saveProductPort.save(Mockito.any())).thenReturn(updatedProduct);

        final ProductResponseDTO actualProductResponseDTO = productService.update(id, productValueObject);

        Assertions.assertEquals(expectedProductResponseDTO.getName(), actualProductResponseDTO.getName());
        Assertions.assertEquals(expectedProductResponseDTO.getCategory(), actualProductResponseDTO.getCategory());
        Assertions.assertEquals(expectedProductResponseDTO.getPrice(), actualProductResponseDTO.getPrice());
        Assertions.assertEquals(expectedProductResponseDTO.getDescription(), actualProductResponseDTO.getDescription());

    }

    @Test
    void updateNotFound() {
        final Long id = 1L;
        final String name = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final ProductValueObject productValueObject = new ProductValueObject(
                name,
                category,
                price,
                description,
                null
        );

        Mockito.when(findProductByIdPort.findProductById(Mockito.eq(id))).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.update(id, productValueObject));
    }

    @Test
    void findProductByCategoryNotFound() {
        final ProductCategory category = ProductCategory.DRINK;

        Mockito.when(findProductByCategoryPort.findProductByCategory(Mockito.eq(category)))
                .thenReturn(Collections.emptyList());

        Assertions.assertThrows(ProductNotFoundException.class,
                () -> productService.findProductByCategory(category));
    }

    @Test
    void findProductByCategory() {
        final String name = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final Product persistedProduct = new Product(
                name,
                category,
                price,
                description,
                null);

        final ProductResponseDTO expectedProductResponseDTO = new ProductResponseDTO();
        expectedProductResponseDTO.setName(name);
        expectedProductResponseDTO.setCategory(category);
        expectedProductResponseDTO.setPrice(price);
        expectedProductResponseDTO.setDescription(description);

        Mockito.when(findProductByCategoryPort.findProductByCategory(Mockito.eq(category)))
                .thenReturn(Collections.singletonList(persistedProduct));

        final List<ProductResponseDTO> actualProductResponseDtoList = productService.findProductByCategory(category);

        final ProductResponseDTO actualProductResponseDTO = actualProductResponseDtoList.get(0);

        Assertions.assertEquals(expectedProductResponseDTO.getName(), actualProductResponseDTO.getName());
        Assertions.assertEquals(expectedProductResponseDTO.getCategory(), actualProductResponseDTO.getCategory());
        Assertions.assertEquals(expectedProductResponseDTO.getPrice(), actualProductResponseDTO.getPrice());
        Assertions.assertEquals(expectedProductResponseDTO.getDescription(), actualProductResponseDTO.getDescription());
    }
}