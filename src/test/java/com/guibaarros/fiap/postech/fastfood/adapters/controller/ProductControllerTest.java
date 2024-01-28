package com.guibaarros.fiap.postech.fastfood.adapters.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guibaarros.fiap.postech.fastfood.application.dtos.product.ProductRequestDTO;
import com.guibaarros.fiap.postech.fastfood.application.dtos.product.ProductResponseDTO;
import com.guibaarros.fiap.postech.fastfood.interfaces.ProductController;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.ProductValueObject;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.enums.ProductCategory;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.product.InvalidProductCategoryException;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.CreateProductUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.DeleteProductUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.FindProductByCategoryUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.UpdateProductUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private CreateProductUseCase createProductUseCase;

    @Mock
    private DeleteProductUseCase deleteProductUseCase;

    @Mock
    private FindProductByCategoryUseCase findProductByCategoryUseCase;

    @Mock
    private UpdateProductUseCase updateProductUseCase;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductController productController;

    @Test
    void createProduct() throws IOException {
        final String requestBody = "{}";
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

        final ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName(name);
        productRequestDTO.setCategory(category);
        productRequestDTO.setPrice(price);
        productRequestDTO.setDescription(description);

        final ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setName(name);
        productResponseDTO.setCategory(category);
        productResponseDTO.setPrice(price);
        productResponseDTO.setDescription(description);

        final ResponseEntity<ProductResponseDTO> expectedResponseEntity =
                ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);

        Mockito.when(objectMapper.readValue(requestBody, ProductRequestDTO.class))
                        .thenReturn(productRequestDTO);
        Mockito.when(createProductUseCase.createProduct(Mockito.eq(productValueObject)))
                .thenReturn(productResponseDTO);

        final ResponseEntity<ProductResponseDTO> actualResponseEntity =
                productController.createProduct(requestBody, null);

        Assertions.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    void updateProduct() throws IOException {
        final String requestBody = "{}";
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

        final ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName(name);
        productRequestDTO.setCategory(category);
        productRequestDTO.setPrice(price);
        productRequestDTO.setDescription(description);

        final ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setName(name);
        productResponseDTO.setCategory(category);
        productResponseDTO.setPrice(price);
        productResponseDTO.setDescription(description);

        final ResponseEntity<ProductResponseDTO> expectedResponseEntity =
                ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);

        Mockito.when(objectMapper.readValue(requestBody, ProductRequestDTO.class))
                .thenReturn(productRequestDTO);
        Mockito.when(updateProductUseCase.update(Mockito.eq(id), Mockito.eq(productValueObject)))
                .thenReturn(productResponseDTO);

        final ResponseEntity<ProductResponseDTO> actualResponseEntity =
                productController.updateProduct(id, requestBody, null);

        Assertions.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    void deleteProduct() {
        final Long id = 1L;

        Mockito.doNothing().when(deleteProductUseCase).delete(Mockito.eq(id));
        final ResponseEntity<Void> expectedResponseEntity = ResponseEntity.ok().build();

        final ResponseEntity<Void> actualResponseEntity = productController.deleteProduct(id);

        Assertions.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    void findProductsByCategory() {
        final String stringCategory = "DRINK";

        final String name = "Água";
        final ProductCategory category = ProductCategory.DRINK;
        final BigDecimal price = BigDecimal.valueOf(3L);
        final String description = "Água mineral sem gás";

        final ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setName(name);
        productResponseDTO.setCategory(category);
        productResponseDTO.setPrice(price);
        productResponseDTO.setDescription(description);

        final List<ProductResponseDTO> productResponseDTOList = Collections.singletonList(productResponseDTO);

        final ResponseEntity<List<ProductResponseDTO>> expectedResponseEntity
                = ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);

        Mockito.when(findProductByCategoryUseCase.findProductByCategory(Mockito.eq(category)))
                .thenReturn(productResponseDTOList);

        final ResponseEntity<List<ProductResponseDTO>> actualResponseEntity = productController.findProductsByCategory(stringCategory);

        Assertions.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    void findProductsByInvalidCategory() {
        final String stringCategory = "LANCHE";

        Assertions.assertThrows(InvalidProductCategoryException.class,
                () -> productController.findProductsByCategory(stringCategory));
    }
}