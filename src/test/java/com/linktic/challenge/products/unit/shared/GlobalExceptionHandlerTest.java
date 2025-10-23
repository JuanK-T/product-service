package com.linktic.challenge.products.unit.shared;

import com.linktic.challenge.shared.exception.GlobalExceptionHandler;
import com.linktic.challenge.shared.response.ErrorDetail;
import com.linktic.challenge.shared.response.StandardResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    // ========== EXCEPCIONES DE SPRING WEB ==========

    @Test
    @DisplayName("Deberia manejar HttpRequestMethodNotSupportedException correctamente")
    void shouldHandleHttpRequestMethodNotSupportedException() {
        // Given
        String method = "PATCH";
        HttpRequestMethodNotSupportedException exception =
                new HttpRequestMethodNotSupportedException(method);

        // When
        ResponseEntity<StandardResponse<Object>> response =
                exceptionHandler.handleMethodNotSupported(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertFalse(responseBody.isSuccess());
        assertEquals("405", responseBody.getCode());
        assertEquals("Operación no permitida", responseBody.getMessage());

        List<ErrorDetail> errors = responseBody.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.size());

        ErrorDetail errorDetail = errors.getFirst();
        assertEquals("METHOD_NOT_ALLOWED", errorDetail.getCode());
        assertEquals("Método no permitido", errorDetail.getMessage());
        assertTrue(errorDetail.getDetails().contains(method));
    }

    @Test
    @DisplayName("Deberia manejar HttpMessageNotReadableException correctamente")
    void shouldHandleHttpMessageNotReadableException() {
        // Given - Usar MockHttpInputMessage
        HttpInputMessage inputMessage = new MockHttpInputMessage(new byte[0]);
        HttpMessageNotReadableException exception =
                new HttpMessageNotReadableException("JSON mal formado",
                        new RuntimeException("Causa"), inputMessage);

        // When
        ResponseEntity<StandardResponse<Object>> response =
                exceptionHandler.handleMessageNotReadable(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("400", responseBody.getCode());
        assertEquals("Formato de datos inválido", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("INVALID_JSON", errorDetail.getCode());
        assertEquals("JSON inválido", errorDetail.getMessage());
        assertEquals("El cuerpo de la solicitud contiene JSON mal formado o incompleto",
                errorDetail.getDetails());
    }

    @Test
    @DisplayName("Deberia manejar MissingServletRequestParameterException correctamente")
    void shouldHandleMissingServletRequestParameterException() {
        // Given
        String parameterName = "productId";
        MissingServletRequestParameterException exception =
                new MissingServletRequestParameterException(parameterName, "Long");

        // When
        ResponseEntity<StandardResponse<Object>> response =
                exceptionHandler.handleMissingParameter(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("400", responseBody.getCode());
        assertEquals("Faltan parámetros requeridos", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("MISSING_PARAMETER", errorDetail.getCode());
        assertEquals("Parámetro requerido faltante", errorDetail.getMessage());
        assertTrue(errorDetail.getDetails().contains(parameterName));
    }

    @Test
    @DisplayName("Deberia manejar NoHandlerFoundException correctamente")
    void shouldHandleNoHandlerFoundException() {
        // Given - Usar un constructor válido sin parámetros nulos
        NoHandlerFoundException exception = new NoHandlerFoundException("GET", "/api/v1/nonexistent",
                new org.springframework.http.HttpHeaders());

        // When
        ResponseEntity<StandardResponse<Object>> response =
                exceptionHandler.handleNoHandlerFound(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("404", responseBody.getCode());
        assertEquals("Recurso no encontrado", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("ENDPOINT_NOT_FOUND", errorDetail.getCode());
        assertEquals("Endpoint no encontrado", errorDetail.getMessage());
        assertTrue(errorDetail.getDetails().contains("/api/v1/nonexistent"));
    }

    // ========== EXCEPCIONES DE JAVA COMUNES ==========

    @Test
    @DisplayName("Deberia manejar IllegalArgumentException correctamente")
    void shouldHandleIllegalArgumentException() {
        // Given
        String errorMessage = "El parámetro no puede ser negativo";
        IllegalArgumentException exception =
                new IllegalArgumentException(errorMessage);

        // When
        ResponseEntity<StandardResponse<Object>> response =
                exceptionHandler.handleIllegalArgument(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("400", responseBody.getCode());
        assertEquals("Parámetros de solicitud inválidos", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("ILLEGAL_ARGUMENT", errorDetail.getCode());
        assertEquals("Argumento inválido", errorDetail.getMessage());
        assertEquals(errorMessage, errorDetail.getDetails());
    }

    @Test
    @DisplayName("Deberia manejar NullPointerException correctamente")
    void shouldHandleNullPointerException() {
        // Given
        NullPointerException exception = new NullPointerException("Object is null");

        // When
        ResponseEntity<StandardResponse<Object>> response =
                exceptionHandler.handleNullPointer(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("500", responseBody.getCode());
        assertEquals("Error interno del sistema", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("INTERNAL_ERROR", errorDetail.getCode());
        assertEquals("Error interno del servidor", errorDetail.getMessage());
        assertEquals("Ocurrió un error inesperado procesando la solicitud",
                errorDetail.getDetails());
    }

    @Test
    @DisplayName("Deberia manejar IllegalStateException correctamente")
    void shouldHandleIllegalStateException() {
        // Given
        String errorMessage = "El sistema no está en estado válido para esta operación";
        IllegalStateException exception = new IllegalStateException(errorMessage);

        // When
        ResponseEntity<StandardResponse<Object>> response =
                exceptionHandler.handleIllegalState(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("409", responseBody.getCode());
        assertEquals("Operación no permitida en el estado actual", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("ILLEGAL_STATE", errorDetail.getCode());
        assertEquals("Estado inválido del sistema", errorDetail.getMessage());
        assertEquals(errorMessage, errorDetail.getDetails());
    }

    // ========== EXCEPCIÓN GENÉRICA ==========

    @Test
    @DisplayName("Deberia manejar Exception genérica correctamente")
    void shouldHandleGenericException() {
        // Given
        String errorMessage = "Error genérico no especificado";
        Exception exception = new Exception(errorMessage);

        // When
        ResponseEntity<StandardResponse<Object>> response =
                exceptionHandler.handleGenericException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("500", responseBody.getCode());
        assertEquals("Error interno del servidor", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("UNEXPECTED_ERROR", errorDetail.getCode());
        assertEquals("Error inesperado", errorDetail.getMessage());
        assertEquals("Ocurrió un error inesperado en el servidor", errorDetail.getDetails());
    }

    // ========== EXCEPCIONES DE BASE DE DATOS ==========

    @Test
    @DisplayName("Deberia manejar DataAccessException correctamente")
    void shouldHandleDataAccessException() {
        // Given
        String errorMessage = "Error de conexión a la base de datos";
        DataAccessException exception = new DataAccessException(errorMessage) {};

        // When
        ResponseEntity<StandardResponse<Object>> response =
                exceptionHandler.handleDataAccessException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("500", responseBody.getCode());
        assertEquals("Error en el almacenamiento de datos", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("DATABASE_ERROR", errorDetail.getCode());
        assertEquals("Error de base de datos", errorDetail.getMessage());
        assertEquals("Ocurrió un error al acceder a los datos", errorDetail.getDetails());
    }

    @Test
    @DisplayName("Deberia incluir metadata en todas las respuestas")
    void shouldIncludeMetadataInAllResponses() {
        // Given - Probar con una excepción cualquiera
        IllegalArgumentException exception =
                new IllegalArgumentException("Test exception");

        // When
        ResponseEntity<StandardResponse<Object>> response =
                exceptionHandler.handleIllegalArgument(exception);

        // Then
        assertNotNull(response);
        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);

        // Verificar que todas las respuestas incluyen metadata estándar
        assertNotNull(responseBody.getCorrelationId(),
                "Todas las respuestas deben incluir correlationId");
        assertNotNull(responseBody.getTimestamp(),
                "Todas las respuestas deben incluir timestamp");
        assertFalse(responseBody.isSuccess(),
                "Las respuestas de error deben tener success=false");
    }

    @Test
    @DisplayName("Deberia manejar diferentes tipos de DataAccessException")
    void shouldHandleDifferentDataAccessExceptions() {
        // Given - Probar diferentes subclases de DataAccessException
        DataAccessException[] exceptions = {
                new DataAccessException("Error de conexión") {},
                new org.springframework.dao.DataIntegrityViolationException("Violación de integridad"),
                new org.springframework.dao.DuplicateKeyException("Llave duplicada"),
                // Corregir el constructor de EmptyResultDataAccessException
                new org.springframework.dao.EmptyResultDataAccessException("Resultado vacío", 1)
        };

        for (DataAccessException exception : exceptions) {
            // When
            ResponseEntity<StandardResponse<Object>> response =
                    exceptionHandler.handleDataAccessException(exception);

            // Then
            assertNotNull(response);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

            StandardResponse<Object> responseBody = response.getBody();
            assertNotNull(responseBody);

            ErrorDetail errorDetail = responseBody.getErrors().getFirst();
            assertEquals("DATABASE_ERROR", errorDetail.getCode());
            assertEquals("Error de base de datos", errorDetail.getMessage());
        }
    }

    // Test adicional para cubrir edge cases
    @Test
    @DisplayName("Deberia manejar RuntimeException no específica")
    void shouldHandleRuntimeException() {
        // Given - RuntimeException no manejada específicamente debería caer en Exception genérica
        RuntimeException exception = new RuntimeException("Runtime error");

        // When
        ResponseEntity<StandardResponse<Object>> response =
                exceptionHandler.handleGenericException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        StandardResponse<Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("500", responseBody.getCode());
        assertEquals("Error interno del servidor", responseBody.getMessage());

        ErrorDetail errorDetail = responseBody.getErrors().getFirst();
        assertEquals("UNEXPECTED_ERROR", errorDetail.getCode());
        assertEquals("Error inesperado", errorDetail.getMessage());
    }
}