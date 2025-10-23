package com.linktic.challenge.products.unit.shared;


import com.linktic.challenge.shared.filter.CorrelationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CorrelationFilterTest {

    @InjectMocks
    private CorrelationFilter correlationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    void shouldGenerateCorrelationIdWhenNotProvided() throws Exception {
        // Given - no header de correlación
        when(request.getHeader(CorrelationFilter.HEADER)).thenReturn(null);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/products");

        // When
        correlationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(response).setHeader(eq(CorrelationFilter.HEADER), anyString());
        verify(filterChain).doFilter(request, response);

        // Verificar que el MDC se limpió
        assertNull(MDC.get("correlationId"));
    }

    @Test
    void shouldUseProvidedCorrelationId() throws Exception {
        // Given - header de correlación proporcionado
        String expectedCorrelationId = "test-correlation-123";
        when(request.getHeader(CorrelationFilter.HEADER)).thenReturn(expectedCorrelationId);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/products/compare");

        // When
        correlationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(response).setHeader(CorrelationFilter.HEADER, expectedCorrelationId);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldGenerateNewCorrelationIdWhenHeaderIsBlank() throws Exception {
        // Given - header vacío o con espacios
        when(request.getHeader(CorrelationFilter.HEADER)).thenReturn("   ");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/products");

        // When
        correlationFilter.doFilterInternal(request, response, filterChain);

        // Then - Debe generar un nuevo UUID (no usar el header vacío)
        verify(response).setHeader(eq(CorrelationFilter.HEADER), anyString());
        // Verificar que NO usa el header vacío
        verify(response, never()).setHeader(CorrelationFilter.HEADER, "   ");
    }

    @Test
    void shouldGenerateNewCorrelationIdWhenHeaderIsEmpty() throws Exception {
        // Given - header vacío
        when(request.getHeader(CorrelationFilter.HEADER)).thenReturn("");
        when(request.getMethod()).thenReturn("PUT");
        when(request.getRequestURI()).thenReturn("/api/products/1");

        // When
        correlationFilter.doFilterInternal(request, response, filterChain);

        // Then - Debe generar un nuevo UUID
        verify(response).setHeader(eq(CorrelationFilter.HEADER), anyString());
        verify(response, never()).setHeader(CorrelationFilter.HEADER, "");
    }

    @Test
    void shouldSetMDCContextCorrectly() throws Exception {
        // Given
        String correlationId = "test-cid-456";
        String method = "DELETE";
        String path = "/api/products/123";

        when(request.getHeader(CorrelationFilter.HEADER)).thenReturn(correlationId);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(path);

        // When
        correlationFilter.doFilterInternal(request, response, filterChain);

        // Then - El MDC se establece temporalmente durante la ejecución
        // Pero se limpia en el finally, así que verificamos indirectamente
        verify(filterChain).doFilter(request, response);

        // Verificar que el request attribute se establece
        verify(request).setAttribute(CorrelationFilter.HEADER, correlationId);
    }

    @Test
    void shouldClearMDCEvenWhenExceptionOccurs() throws Exception {
        // Given - simular excepción en el filterChain
        when(request.getHeader(CorrelationFilter.HEADER)).thenReturn("test-cid");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/products");

        doThrow(new RuntimeException("Test exception")).when(filterChain).doFilter(request, response);

        // When & Then
        assertThrows(RuntimeException.class, () ->
                correlationFilter.doFilterInternal(request, response, filterChain)
        );

        // El MDC debe limpiarse incluso con excepción
        assertNull(MDC.get("correlationId"));
        assertNull(MDC.get("method"));
        assertNull(MDC.get("path"));
    }

    @Test
    void shouldSetRequestAttributeWithCorrelationId() throws Exception {
        // Given
        String correlationId = "request-attr-test";
        when(request.getHeader(CorrelationFilter.HEADER)).thenReturn(correlationId);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/test");

        // When
        correlationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(request).setAttribute(CorrelationFilter.HEADER, correlationId);
    }

    @Test
    void shouldHandleNullRequestMethodAndPathGracefully() throws Exception {
        // Given - métodos y path nulos
        when(request.getHeader(CorrelationFilter.HEADER)).thenReturn("test-cid");
        when(request.getMethod()).thenReturn(null);
        when(request.getRequestURI()).thenReturn(null);

        // When - No debe lanzar excepción
        assertDoesNotThrow(() ->
                correlationFilter.doFilterInternal(request, response, filterChain)
        );

        // Then
        verify(filterChain).doFilter(request, response);
    }
}
