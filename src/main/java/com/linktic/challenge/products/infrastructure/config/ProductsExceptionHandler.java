package com.linktic.challenge.products.infrastructure.config;

import com.linktic.challenge.products.domain.exception.ProductDomainException;
import com.linktic.challenge.products.domain.exception.entity.ProductAlreadyExistsException;
import com.linktic.challenge.products.domain.exception.entity.ProductNotFoundException;
import com.linktic.challenge.products.domain.exception.entity.InvalidProductException;
import com.linktic.challenge.products.domain.exception.mapper.ProductMapperException;
import com.linktic.challenge.products.domain.exception.valueobject.*;
import com.linktic.challenge.products.infrastructure.web.ProductController;
import com.linktic.challenge.shared.response.ErrorDetail;
import com.linktic.challenge.shared.response.StandardResponse;
import com.linktic.challenge.shared.util.StandardResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice(assignableTypes = ProductController.class)
@Slf4j
public class ProductsExceptionHandler {

    // ========== EXCEPCIONES DE ENTIDAD ==========

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<StandardResponse<Object>> handleProductNotFoundException(ProductNotFoundException ex) {
        log.warn("Producto no encontrado: {}", ex.getMessage());

        ErrorDetail errorDetail = StandardResponses.errorDetail(
                "PRODUCT_NOT_FOUND",
                "Producto no encontrado",
                ex.getMessage()
        );

        StandardResponse<Object> response = StandardResponses.errorResponse(
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                "No se pudo encontrar el producto solicitado",
                List.of(errorDetail)
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<StandardResponse<Object>> handleProductAlreadyExistsException(ProductAlreadyExistsException ex) {
        log.warn("Producto ya existe: {}", ex.getMessage());

        ErrorDetail errorDetail = StandardResponses.errorDetail(
                "PRODUCT_ALREADY_EXISTS",
                "Producto duplicado",
                ex.getMessage()
        );

        StandardResponse<Object> response = StandardResponses.errorResponse(
                String.valueOf(HttpStatus.CONFLICT.value()),
                "Ya existe un producto con los mismos datos",
                List.of(errorDetail)
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<StandardResponse<Object>> handleInvalidProductException(InvalidProductException ex) {
        log.warn("Producto inválido: {}", ex.getMessage());

        ErrorDetail errorDetail = StandardResponses.errorDetail(
                "INVALID_PRODUCT",
                "Datos del producto inválidos",
                ex.getMessage()
        );

        StandardResponse<Object> response = StandardResponses.errorResponse(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "Los datos del producto no son válidos",
                List.of(errorDetail)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ========== EXCEPCIONES DE VALUE OBJECTS ==========

    @ExceptionHandler({
            InvalidProductIdException.class,
            InvalidProductNameException.class,
            InvalidProductDescriptionException.class,
            InvalidBrandException.class,
            InvalidCategoryException.class,
            InvalidPriceException.class,
            InvalidRatingException.class,
            InvalidImageUrlException.class,
            InvalidSpecificationsException.class
    })
    public ResponseEntity<StandardResponse<Object>> handleValueObjectExceptions(ProductDomainException ex) {
        log.warn("Error en value object de producto: {}", ex.getMessage());

        String errorCode = resolveValueObjectErrorCode(ex);
        String userMessage = resolveValueObjectUserMessage(ex);

        ErrorDetail errorDetail = StandardResponses.errorDetail(
                errorCode,
                userMessage,
                ex.getMessage()
        );

        StandardResponse<Object> response = StandardResponses.errorResponse(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "Datos de producto inválidos",
                List.of(errorDetail)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ========== EXCEPCIONES DE MAPPER ==========

    @ExceptionHandler(ProductMapperException.class)
    public ResponseEntity<StandardResponse<Object>> handleProductMapperException(ProductMapperException ex) {
        log.error("Error en mapeo de producto: {}", ex.getMessage(), ex);

        ErrorDetail errorDetail = StandardResponses.errorDetail(
                "PRODUCT_MAPPING_ERROR",
                "Error en procesamiento de datos",
                "Ocurrió un error al procesar los datos del producto"
        );

        StandardResponse<Object> response = StandardResponses.errorResponse(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "Error interno en el procesamiento de datos",
                List.of(errorDetail)
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // ========== EXCEPCIONES GENÉRICAS DE DOMINIO ==========

    @ExceptionHandler(ProductDomainException.class)
    public ResponseEntity<StandardResponse<Object>> handleGenericProductDomainException(ProductDomainException ex) {
        log.warn("Excepción de dominio de producto: {}", ex.getMessage());

        ErrorDetail errorDetail = StandardResponses.errorDetail(
                "PRODUCT_DOMAIN_ERROR",
                "Error de negocio",
                ex.getMessage()
        );

        StandardResponse<Object> response = StandardResponses.errorResponse(
                String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                "No se pudo procesar la operación solicitada",
                List.of(errorDetail)
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    // ========== EXCEPCIONES DE VALIDACIÓN DE SPRING ==========

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Error de validación en request: {}", ex.getMessage());

        List<ErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> StandardResponses.errorDetail(
                        "VALIDATION_ERROR",
                        String.format("Campo '%s' %s", fieldError.getField(), fieldError.getDefaultMessage()),
                        String.format("Campo: %s, Valor rechazado: %s",
                                fieldError.getField(),
                                fieldError.getRejectedValue())
                ))
                .toList();

        StandardResponse<Object> response = StandardResponses.errorResponse(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "Errores de validación en los datos de entrada",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardResponse<Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Error de tipo en argumento: {}", ex.getMessage());

        ErrorDetail errorDetail = StandardResponses.errorDetail(
                "INVALID_PARAMETER_TYPE",
                "Tipo de parámetro inválido",
                String.format("El parámetro '%s' debe ser de tipo %s",
                        ex.getName(),
                        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido")
        );

        StandardResponse<Object> response = StandardResponses.errorResponse(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                "Error en los parámetros de la solicitud",
                List.of(errorDetail)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ========== MÉTODOS AUXILIARES PRIVADOS ==========

    private String resolveValueObjectErrorCode(ProductDomainException ex) {
        if (ex instanceof InvalidProductIdException) return "INVALID_PRODUCT_ID";
        if (ex instanceof InvalidProductNameException) return "INVALID_PRODUCT_NAME";
        if (ex instanceof InvalidProductDescriptionException) return "INVALID_PRODUCT_DESCRIPTION";
        if (ex instanceof InvalidBrandException) return "INVALID_BRAND";
        if (ex instanceof InvalidCategoryException) return "INVALID_CATEGORY";
        if (ex instanceof InvalidPriceException) return "INVALID_PRICE";
        if (ex instanceof InvalidRatingException) return "INVALID_RATING";
        if (ex instanceof InvalidImageUrlException) return "INVALID_IMAGE_URL";
        if (ex instanceof InvalidSpecificationsException) return "INVALID_SPECIFICATIONS";
        return "INVALID_PRODUCT_DATA";
    }

    private String resolveValueObjectUserMessage(ProductDomainException ex) {
        if (ex instanceof InvalidProductIdException) return "ID de producto inválido";
        if (ex instanceof InvalidProductNameException) return "Nombre de producto inválido";
        if (ex instanceof InvalidProductDescriptionException) return "Descripción de producto inválida";
        if (ex instanceof InvalidBrandException) return "Marca inválida";
        if (ex instanceof InvalidCategoryException) return "Categoría inválida";
        if (ex instanceof InvalidPriceException) return "Precio inválido";
        if (ex instanceof InvalidRatingException) return "Calificación inválida";
        if (ex instanceof InvalidImageUrlException) return "URL de imagen inválida";
        if (ex instanceof InvalidSpecificationsException) return "Especificaciones inválidas";
        return "Datos de producto inválidos";
    }
}