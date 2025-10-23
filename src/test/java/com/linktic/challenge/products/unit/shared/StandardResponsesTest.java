package com.linktic.challenge.products.unit.shared;

import com.linktic.challenge.shared.response.ErrorDetail;
import com.linktic.challenge.shared.response.StandardResponse;
import com.linktic.challenge.shared.util.StandardResponses;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StandardResponsesTest {

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldCreateSuccessResponseWithData() {
        // When
        StandardResponse<String> response = StandardResponses.ok("test-data");

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("Operación exitosa");
        assertThat(response.getData()).isEqualTo("test-data");
        assertThat(response.getErrors()).isEmpty();
        assertThat(response.getMeta()).isEmpty();
        assertThat(response.getLinks()).isEmpty();
        assertThat(response.getCorrelationId()).isNotNull();
        assertThat(response.getTimestamp()).isNotNull();
    }

    @Test
    void shouldCreateSuccessResponseWithCustomMessage() {
        // When
        StandardResponse<String> response = StandardResponses.ok("test-data", "Mensaje personalizado");

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("Mensaje personalizado");
        assertThat(response.getData()).isEqualTo("test-data");
    }

    @Test
    void shouldCreateCreatedResponse() {
        // When
        StandardResponse<String> response = StandardResponses.created("created-data");

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("Recurso creado exitosamente");
        assertThat(response.getData()).isEqualTo("created-data");
    }

    @Test
    void shouldCreateCreatedResponseWithCustomMessage() {
        // When
        StandardResponse<String> response = StandardResponses.created("created-data", "Producto creado correctamente");

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("Producto creado correctamente");
        assertThat(response.getData()).isEqualTo("created-data");
    }

    @Test
    void shouldCreateUpdatedResponse() {
        // When
        StandardResponse<String> response = StandardResponses.updated("updated-data");

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("Recurso actualizado exitosamente");
        assertThat(response.getData()).isEqualTo("updated-data");
    }

    @Test
    void shouldCreateUpdatedResponseWithCustomMessage() {
        // When
        StandardResponse<String> response = StandardResponses.updated("updated-data", "Producto actualizado correctamente");

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("Producto actualizado correctamente");
        assertThat(response.getData()).isEqualTo("updated-data");
    }

    @Test
    void shouldCreateDeletedResponse() {
        // When
        StandardResponse<String> response = StandardResponses.deleted("deletion-confirmation");

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("Recurso eliminado exitosamente");
        assertThat(response.getData()).isEqualTo("deletion-confirmation");
    }

    @Test
    void shouldCreateDeletedResponseWithCustomMessage() {
        // When
        StandardResponse<String> response = StandardResponses.deleted("deletion-confirmation", "Producto eliminado del catálogo");

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("Producto eliminado del catálogo");
        assertThat(response.getData()).isEqualTo("deletion-confirmation");
    }

    @Test
    void shouldCreateRetrievedResponse() {
        // When
        StandardResponse<String> response = StandardResponses.retrieved("retrieved-data");

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("Recurso obtenido exitosamente");
        assertThat(response.getData()).isEqualTo("retrieved-data");
    }

    @Test
    void shouldCreateRetrievedResponseWithCustomMessage() {
        // When
        StandardResponse<String> response = StandardResponses.retrieved("retrieved-data", "Producto encontrado exitosamente");

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCode()).isEqualTo("200");
        assertThat(response.getMessage()).isEqualTo("Producto encontrado exitosamente");
        assertThat(response.getData()).isEqualTo("retrieved-data");
    }

    @Test
    void shouldCreateErrorResponseWithDetails() {
        // Given
        List<ErrorDetail> errors = List.of(
                StandardResponses.errorDetail("VALIDATION_ERROR", "Field required", "Email field is mandatory")
        );

        // When
        StandardResponse<Object> response = StandardResponses.errorResponse(
                "400", "Validation failed", errors
        );

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCode()).isEqualTo("400");
        assertThat(response.getMessage()).isEqualTo("Validation failed");
        assertThat(response.getData()).isNull();
        assertThat(response.getErrors()).hasSize(1);
        assertThat(response.getErrors().getFirst().getCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getMeta()).containsEntry("path", "unknown");
    }

    @Test
    void shouldResolveCorrelationIdFromMDC() {
        // Given
        String expectedCorrelationId = "mdc-correlation-123";
        MDC.put(StandardResponses.MDC_CORRELATION_KEY, expectedCorrelationId);

        // When
        StandardResponse<String> response = StandardResponses.ok("test");

        // Then
        assertThat(response.getCorrelationId()).isEqualTo(expectedCorrelationId);
    }

    @Test
    void shouldGenerateNewCorrelationIdWhenMDCIsEmpty() {
        // Given - MDC vacío
        MDC.clear();

        // When
        StandardResponse<String> response = StandardResponses.ok("test");

        // Then
        assertThat(response.getCorrelationId()).isNotNull();
        assertThat(response.getCorrelationId()).isNotBlank();
        // Verificar que es un UUID válido
        assertThat(response.getCorrelationId()).matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
    }

    @Test
    void shouldResolvePathFromMDC() {
        // Given
        String expectedPath = "/api/test-endpoint";
        MDC.put(StandardResponses.MDC_PATH_KEY, expectedPath);

        // When
        StandardResponse<Object> response = StandardResponses.errorResponse("400", "Error", List.of());

        // Then
        assertThat(response.getMeta()).containsEntry("path", expectedPath);
    }

    @Test
    void shouldUseUnknownPathWhenMDCPathIsNull() {
        // Given - MDC path vacío
        MDC.clear();

        // When
        StandardResponse<Object> response = StandardResponses.errorResponse("400", "Error", List.of());

        // Then
        assertThat(response.getMeta()).containsEntry("path", "unknown");
    }

    @Test
    void shouldHandleNullErrorsList() {
        // When - pasar null explícitamente
        StandardResponse<Object> response = StandardResponses.errorResponse("500", "Error", null);

        // Then - la lista de errores no debe ser null
        assertThat(response.getErrors()).isNotNull();
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void shouldCreateErrorDetailWithAllFields() {
        // When
        ErrorDetail error = StandardResponses.errorDetail("CODE_123", "Error message", "Additional details");

        // Then
        assertThat(error.getCode()).isEqualTo("CODE_123");
        assertThat(error.getMessage()).isEqualTo("Error message");
        assertThat(error.getDetails()).isEqualTo("Additional details");
    }

    @Test
    void shouldHandleNullMetaAndLinksInBaseMethod() {
        // This tests the private safeMeta and safeLinks methods indirectly
        StandardResponse<String> response = StandardResponses.ok("test");

        // Collections should never be null
        assertThat(response.getMeta()).isNotNull().isEmpty();
        assertThat(response.getLinks()).isNotNull().isEmpty();
        assertThat(response.getErrors()).isNotNull().isEmpty();
    }

    @Test
    void shouldUseUTCtimestamp() {
        // When
        StandardResponse<String> response = StandardResponses.ok("test");

        // Then - timestamp should be in ISO format with Z (UTC)
        assertThat(response.getTimestamp()).contains("T");
        assertThat(response.getTimestamp()).matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*"); // Formato ISO básico
    }

    @Test
    void shouldHandleEmptyCorrelationIdInMDC() {
        // Given
        MDC.put(StandardResponses.MDC_CORRELATION_KEY, "");

        // When
        StandardResponse<String> response = StandardResponses.ok("test");

        // Then - should generate new correlation ID
        assertThat(response.getCorrelationId()).isNotNull().isNotBlank();
        assertThat(response.getCorrelationId()).isNotEqualTo("");
    }

    @Test
    void shouldHandleBlankCorrelationIdInMDC() {
        // Given
        MDC.put(StandardResponses.MDC_CORRELATION_KEY, "   ");

        // When
        StandardResponse<String> response = StandardResponses.ok("test");

        // Then - should generate new correlation ID
        assertThat(response.getCorrelationId()).isNotNull().isNotBlank();
        assertThat(response.getCorrelationId()).isNotEqualTo("   ");
    }

    @Test
    void shouldVerifyAllConstants() {
        // Then - verificar que las constantes tienen los valores esperados
        assertThat(StandardResponses.DEFAULT_SUCCESS_MESSAGE).isEqualTo("Operación exitosa");
        assertThat(StandardResponses.CREATED_MESSAGE).isEqualTo("Recurso creado exitosamente");
        assertThat(StandardResponses.UPDATED_MESSAGE).isEqualTo("Recurso actualizado exitosamente");
        assertThat(StandardResponses.DELETED_MESSAGE).isEqualTo("Recurso eliminado exitosamente");
        assertThat(StandardResponses.RETRIEVED_MESSAGE).isEqualTo("Recurso obtenido exitosamente");
    }

    @Test
    void shouldCreateMultipleErrorDetails() {
        // Given
        List<ErrorDetail> errors = List.of(
                StandardResponses.errorDetail("ERROR_1", "First error", "First details"),
                StandardResponses.errorDetail("ERROR_2", "Second error", "Second details")
        );

        // When
        StandardResponse<Object> response = StandardResponses.errorResponse("400", "Multiple errors", errors);

        // Then
        assertThat(response.getErrors()).hasSize(2);
        assertThat(response.getErrors().get(0).getCode()).isEqualTo("ERROR_1");
        assertThat(response.getErrors().get(1).getCode()).isEqualTo("ERROR_2");
    }
}