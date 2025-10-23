package com.linktic.challenge.products.infrastructure.web;

import com.linktic.challenge.products.application.dto.CreateProductDto;
import com.linktic.challenge.products.application.dto.ProductDto;
import com.linktic.challenge.products.application.dto.UpdateProductDto;
import com.linktic.challenge.products.application.mapper.ProductMapper;
import com.linktic.challenge.products.application.port.in.ProductManagementUseCase;
import com.linktic.challenge.products.application.port.in.ProductQueryUseCase;
import com.linktic.challenge.products.domain.model.Product;
import com.linktic.challenge.shared.response.StandardResponse;
import com.linktic.challenge.shared.util.StandardResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Productos", description = "API para gestión de productos - Catálogo completo")
public class ProductController {
    private final ProductManagementUseCase productManagementUseCase;
    private final ProductQueryUseCase productQueryUseCase;
    private final ProductMapper productMapper;

    @GetMapping("/{id}")
    public StandardResponse<ProductDto> getProductById(@PathVariable String id) {
        Product product = productQueryUseCase.findById(id);
        ProductDto productDto = productMapper.toDto(product);
        return StandardResponses.retrieved(productDto, "Producto encontrado exitosamente");
    }

    @GetMapping
    public StandardResponse<PageResponse<ProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productQueryUseCase.findAllProducts(pageable);

        Page<ProductDto> productDtos = products.map(productMapper::toDto);
        PageResponse<ProductDto> response = PageResponse.of(productDtos);

        return StandardResponses.retrieved(response, "Lista de productos obtenida exitosamente");
    }

    @PostMapping
    public StandardResponse<ProductDto> createProduct(@RequestBody @Validated CreateProductDto createProductDto) {
        Product product = productMapper.toDomain(createProductDto);
        Product createdProduct = productManagementUseCase.createProduct(product);
        ProductDto createdProductDto = productMapper.toDto(createdProduct);
        return StandardResponses.created(createdProductDto, "Producto creado exitosamente");
    }

    @PutMapping("/{id}")
    public StandardResponse<ProductDto> updateProduct(@PathVariable String id, @RequestBody @Validated UpdateProductDto updateProductDto) {
        Product product = productMapper.toDomain(id, updateProductDto);
        Product updatedProduct = productManagementUseCase.updateProduct(product);
        ProductDto updatedProductDto = productMapper.toDto(updatedProduct);
        return StandardResponses.updated(updatedProductDto, "Producto actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    public StandardResponse<String> deleteProduct(@PathVariable String id) {
        productManagementUseCase.deleteProduct(id);
        return StandardResponses.deleted("Producto eliminado exitosamente", "Producto eliminado del catálogo");
    }
}