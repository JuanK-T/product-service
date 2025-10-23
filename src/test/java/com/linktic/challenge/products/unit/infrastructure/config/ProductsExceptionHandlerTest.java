package com.linktic.challenge.products.unit.infrastructure.config;

import com.linktic.challenge.products.domain.exception.ProductDomainException;
import com.linktic.challenge.products.domain.exception.entity.ProductAlreadyExistsException;
import com.linktic.challenge.products.domain.exception.entity.ProductNotFoundException;
import com.linktic.challenge.products.domain.exception.entity.InvalidProductException;
import com.linktic.challenge.products.domain.exception.mapper.ProductMapperException;
import com.linktic.challenge.products.domain.exception.valueobject.*;
import com.linktic.challenge.products.infrastructure.config.ProductsExceptionHandler;
import com.linktic.challenge.shared.response.ErrorDetail;
import com.linktic.challenge.shared.response.StandardResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductsExceptionHandlerTest {

    @InjectMocks
    private ProductsExceptionHandler exceptionHandler;

    @Test
    @DisplayName("Deberia manejar ProductNotFoundException correctamente")
    void shouldHandleProductNotFoundException() {
        // Given
        String errorMessage = "Producto con ID 123 no encontrado";
        ProductNotFoundException exception = new ProductNotFoundException(errorMessage);

        // When
        ResponseEntity<StandardResponse<Object>> response = exceptionHandler.handleProductNotFoundException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertFalse(responseBody.isSuccess());
        assertEquals("404", responseBody.getCode());
        assertEquals("No se pudo encontrar el producto solicitado", responseBody.getMessage());

        List<ErrorDetail> errors = responseBody.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());

        ErrorDetail errorDetail = errors.getFirst();
        assertEquals("PRODUCT_NOT_FOUND", errorDetail.getCode());
        assertEquals("Producto no encontrado", errorDetail.getMessage());
        // VERIFICAR QUE CONTIENE EL MENSAJE ESPERADO
        assertTrue(errorDetail.getDetails().contains(errorMessage),
                "El mensaje debería contener: " + errorMessage);
        assertTrue(errorDetail.getDetails().contains("Product not found with ID:"),
                "El mensaje debería contener el prefijo de la excepción");
    }

    @Test
    @DisplayName("Deberia manejar ProductAlreadyExistsException correctamente")
    void shouldHandleProductAlreadyExistsException() {
        // Given
        String errorMessage = "Producto con nombre 'Test' ya existe";
        ProductAlreadyExistsException exception = new ProductAlreadyExistsException(errorMessage);

        // When
        ResponseEntity<StandardResponse<Object>> response = exceptionHandler.handleProductAlreadyExistsException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertFalse(responseBody.isSuccess());
        assertEquals("409", responseBody.getCode());
        assertEquals("Ya existe un producto con los mismos datos", responseBody.getMessage());

        List<ErrorDetail> errors = responseBody.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());

        ErrorDetail errorDetail = errors.getFirst();
        assertEquals("PRODUCT_ALREADY_EXISTS", errorDetail.getCode());
        assertEquals("Producto duplicado", errorDetail.getMessage());
        // VERIFICAR QUE CONTIENE EL MENSAJE ESPERADO
        assertTrue(errorDetail.getDetails().contains(errorMessage),
                "El mensaje debería contener: " + errorMessage);
        assertTrue(errorDetail.getDetails().contains("Product already exists with name:"),
                "El mensaje debería contener el prefijo de la excepción");
    }

    @Test
    @DisplayName("Deberia manejar InvalidProductException correctamente")
    void shouldHandleInvalidProductException() {
        // Given
        String errorMessage = "El producto no tiene nombre válido";
        InvalidProductException exception = new InvalidProductException(errorMessage);

        // When
        ResponseEntity<StandardResponse<Object>> response = exceptionHandler.handleInvalidProductException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("400", responseBody.getCode());
        assertEquals("Los datos del producto no son válidos", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("INVALID_PRODUCT", errorDetail.getCode());
        assertEquals("Datos del producto inválidos", errorDetail.getMessage());
        assertEquals(errorMessage, errorDetail.getDetails());
    }

    @Test
    @DisplayName("Deberia manejar InvalidProductIdException correctamente")
    void shouldHandleInvalidProductIdException() {
        // Given
        String errorMessage = "ID de producto inválido: -1";
        InvalidProductIdException exception = new InvalidProductIdException(errorMessage);

        // When
        ResponseEntity<StandardResponse<Object>> response = exceptionHandler.handleValueObjectExceptions(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("400", responseBody.getCode());
        assertEquals("Datos de producto inválidos", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("INVALID_PRODUCT_ID", errorDetail.getCode());
        assertEquals("ID de producto inválido", errorDetail.getMessage());
        assertEquals(errorMessage, errorDetail.getDetails());
    }

    @Test
    @DisplayName("Deberia manejar ProductMapperException correctamente")
    void shouldHandleProductMapperException() {
        // Given
        String errorMessage = "Error mapeando producto";
        ProductMapperException exception = new ProductMapperException(errorMessage);

        // When
        ResponseEntity<StandardResponse<Object>> response = exceptionHandler.handleProductMapperException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("500", responseBody.getCode());
        assertEquals("Error interno en el procesamiento de datos", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("PRODUCT_MAPPING_ERROR", errorDetail.getCode());
        assertEquals("Error en procesamiento de datos", errorDetail.getMessage());
        assertEquals("Ocurrió un error al procesar los datos del producto", errorDetail.getDetails());
    }

    @Test
    @DisplayName("Deberia manejar MethodArgumentNotValidException correctamente")
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("object", "name", "no puede estar vacío");
        FieldError fieldError2 = new FieldError("object", "price", "debe ser positivo");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // When
        ResponseEntity<StandardResponse<Object>> response = exceptionHandler.handleValidationExceptions(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("400", responseBody.getCode());
        assertEquals("Errores de validación en los datos de entrada", responseBody.getMessage());

        List<ErrorDetail> errors = responseBody.getErrors();
        assertEquals(2, errors.size());

        // Verificar que los errores contienen la información esperada
        assertTrue(errors.stream().anyMatch(error ->
                error.getCode().equals("VALIDATION_ERROR") &&
                        error.getMessage().contains("name")
        ));
        assertTrue(errors.stream().anyMatch(error ->
                error.getCode().equals("VALIDATION_ERROR") &&
                        error.getMessage().contains("price")
        ));
    }

    @Test
    @DisplayName("Deberia manejar MethodArgumentTypeMismatchException correctamente")
    void shouldHandleMethodArgumentTypeMismatchException() {
        // Given
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("productId");
        when(exception.getRequiredType()).thenAnswer(invocation -> Long.class);

        // When
        ResponseEntity<StandardResponse<Object>> response = exceptionHandler.handleMethodArgumentTypeMismatch(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("400", responseBody.getCode());
        assertEquals("Error en los parámetros de la solicitud", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("INVALID_PARAMETER_TYPE", errorDetail.getCode());
        assertEquals("Tipo de parámetro inválido", errorDetail.getMessage());
        assertTrue(errorDetail.getDetails().contains("productId"));
        assertTrue(errorDetail.getDetails().contains("Long"));
    }

    @Test
    @DisplayName("Deberia manejar ProductDomainException genérica correctamente")
    void shouldHandleGenericProductDomainException() {
        // Given
        String errorMessage = "Error genérico de dominio";
        ProductDomainException exception = new ProductDomainException(errorMessage) {};

        // When
        ResponseEntity<StandardResponse<Object>> response = exceptionHandler.handleGenericProductDomainException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("422", responseBody.getCode());
        assertEquals("No se pudo procesar la operación solicitada", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("PRODUCT_DOMAIN_ERROR", errorDetail.getCode());
        assertEquals("Error de negocio", errorDetail.getMessage());
        assertEquals(errorMessage, errorDetail.getDetails());
    }

    @Test
    @DisplayName("Deberia cubrir todos los branches de resolveValueObjectErrorCode")
    void shouldCoverAllBranchesOfResolveValueObjectErrorCode() {
        // Este test verifica que todos los if-else en resolveValueObjectErrorCode están cubiertos
        assertEquals("INVALID_PRODUCT_ID", invokeResolveValueObjectErrorCode(new InvalidProductIdException("test")));
        assertEquals("INVALID_PRODUCT_NAME", invokeResolveValueObjectErrorCode(new InvalidProductNameException("test")));
        assertEquals("INVALID_PRODUCT_DESCRIPTION", invokeResolveValueObjectErrorCode(new InvalidProductDescriptionException("test")));
        assertEquals("INVALID_BRAND", invokeResolveValueObjectErrorCode(new InvalidBrandException("test")));
        assertEquals("INVALID_CATEGORY", invokeResolveValueObjectErrorCode(new InvalidCategoryException("test")));
        assertEquals("INVALID_PRICE", invokeResolveValueObjectErrorCode(new InvalidPriceException("test")));
        assertEquals("INVALID_RATING", invokeResolveValueObjectErrorCode(new InvalidRatingException("test")));
        assertEquals("INVALID_IMAGE_URL", invokeResolveValueObjectErrorCode(new InvalidImageUrlException("test")));
        assertEquals("INVALID_SPECIFICATIONS", invokeResolveValueObjectErrorCode(new InvalidSpecificationsException("test")));
        assertEquals("INVALID_PRODUCT_DATA", invokeResolveValueObjectErrorCode(new ProductDomainException("test") {}));
    }

    @Test
    @DisplayName("Deberia cubrir todos los branches de resolveValueObjectUserMessage")
    void shouldCoverAllBranchesOfResolveValueObjectUserMessage() {
        // Este test verifica que todos los if-else en resolveValueObjectUserMessage están cubiertos
        assertEquals("ID de producto inválido", invokeResolveValueObjectUserMessage(new InvalidProductIdException("test")));
        assertEquals("Nombre de producto inválido", invokeResolveValueObjectUserMessage(new InvalidProductNameException("test")));
        assertEquals("Descripción de producto inválida", invokeResolveValueObjectUserMessage(new InvalidProductDescriptionException("test")));
        assertEquals("Marca inválida", invokeResolveValueObjectUserMessage(new InvalidBrandException("test")));
        assertEquals("Categoría inválida", invokeResolveValueObjectUserMessage(new InvalidCategoryException("test")));
        assertEquals("Precio inválido", invokeResolveValueObjectUserMessage(new InvalidPriceException("test")));
        assertEquals("Calificación inválida", invokeResolveValueObjectUserMessage(new InvalidRatingException("test")));
        assertEquals("URL de imagen inválida", invokeResolveValueObjectUserMessage(new InvalidImageUrlException("test")));
        assertEquals("Especificaciones inválidas", invokeResolveValueObjectUserMessage(new InvalidSpecificationsException("test")));
        assertEquals("Datos de producto inválidos", invokeResolveValueObjectUserMessage(new ProductDomainException("test") {}));
    }

    // Métodos auxiliares para acceder a métodos privados usando reflection
    private String invokeResolveValueObjectErrorCode(ProductDomainException ex) {
        try {
            var method = ProductsExceptionHandler.class.getDeclaredMethod("resolveValueObjectErrorCode", ProductDomainException.class);
            method.setAccessible(true);
            return (String) method.invoke(exceptionHandler, ex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String invokeResolveValueObjectUserMessage(ProductDomainException ex) {
        try {
            var method = ProductsExceptionHandler.class.getDeclaredMethod("resolveValueObjectUserMessage", ProductDomainException.class);
            method.setAccessible(true);
            return (String) method.invoke(exceptionHandler, ex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}