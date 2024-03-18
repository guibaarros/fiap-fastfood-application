package com.guibaarros.fiap.postech.fastfood.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guibaarros.fiap.postech.fastfood.infrastructure.web.exceptionhandler.ErrorDTO;
import com.guibaarros.fiap.postech.fastfood.application.dtos.product.ProductRequestDTO;
import com.guibaarros.fiap.postech.fastfood.application.dtos.product.ProductResponseDTO;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.ProductValueObject;
import com.guibaarros.fiap.postech.fastfood.domain.entities.product.enums.ProductCategory;
import com.guibaarros.fiap.postech.fastfood.application.exceptions.product.InvalidProductCategoryException;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.CreateProductUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.DeleteProductUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.FindProductByCategoryUseCase;
import com.guibaarros.fiap.postech.fastfood.application.usecases.product.UpdateProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
@Tag(name = "Product Controller")
@Validated
@Slf4j
public class ProductRestController {

    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final FindProductByCategoryUseCase findProductByCategoryUseCase;
    private final UpdateProductUseCase updateProductUseCase;

    private final ObjectMapper objectMapper;

    @Operation(summary = "Cadastrar um novo produto")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Produto cadastrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class))}),
            @ApiResponse(responseCode = "422",
                    description = "Produto já cadastrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestParam("body") final String requestBody,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        final ProductRequestDTO productRequestDTO = objectMapper.readValue(requestBody, ProductRequestDTO.class);
        log.info("create new product; productRequestDTO={}", productRequestDTO);

        final ProductValueObject productValueObject = new ProductValueObject(
                productRequestDTO.getName(),
                productRequestDTO.getCategory(),
                productRequestDTO.getPrice(),
                productRequestDTO.getDescription(),
                file
        );
        final ProductResponseDTO productResponseDTO = createProductUseCase.createProduct(productValueObject);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    @Operation(summary = "Atualiza um produto")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Produto atualizado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Produto não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable("id") final Long id,
            @RequestParam("body") final String requestBody,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        final ProductRequestDTO productRequestDTO = objectMapper.readValue(requestBody, ProductRequestDTO.class);
        log.info("update product; productRequestDTO={}", productRequestDTO);

        final ProductValueObject productValueObject = new ProductValueObject(
                productRequestDTO.getName(),
                productRequestDTO.getCategory(),
                productRequestDTO.getPrice(),
                productRequestDTO.getDescription(),
                file
        );
        final ProductResponseDTO productResponseDTO = updateProductUseCase.update(id, productValueObject);
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }

    @Operation(summary = "Remove um produto")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Produto removido"),
            @ApiResponse(responseCode = "404",
                    description = "Produto não encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") final Long id) {
        log.info("delete product; id={}", id);
        deleteProductUseCase.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Busca produtos por categoria")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Produto(s) encontrado(s)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Categoria inválida",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductCategory.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Produto(s) não encontrado(s)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponseDTO>> findProductsByCategory(@PathParam("category") final String category) {
        log.info("find products by category; category={}", category);
        try {
            ProductCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidProductCategoryException(category);
        }

        final List<ProductResponseDTO> productResponseDTOList =
                findProductByCategoryUseCase.findProductByCategory(ProductCategory.valueOf(category.toUpperCase()));
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTOList);
    }
}
