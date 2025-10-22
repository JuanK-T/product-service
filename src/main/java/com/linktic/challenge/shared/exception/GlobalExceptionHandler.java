package com.linktic.challenge.shared.exception;

import com.linktic.challenge.shared.response.StandardResponse;
import com.linktic.challenge.shared.util.StandardResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StandardResponse<Object> badRequest(MethodArgumentNotValidException ex) {
        var fields = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> StandardResponses.errorDetail("VALIDATION_ERROR",
                        "Campo '%s': %s".formatted(fe.getField(), fe.getDefaultMessage()),
                        fe.getObjectName()))
                .toList();

        return StandardResponses.errorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                "Error de validación en los datos de entrada",
                fields
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public StandardResponse<Object> notFound(NoResourceFoundException ex) {

        var ed = StandardResponses.errorDetail("RESOURCE_NOT_FOUND",
                ex.getMessage(), null);

        return StandardResponses.errorResponse(
                HttpStatus.NOT_FOUND.toString(),
                "Recurso no encontrado",
                List.of(ed)
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public StandardResponse<Object> methodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                       HttpServletResponse response) {
        // 1) Evita NPE guardando en una variable
        Set<HttpMethod> methods = ex.getSupportedHttpMethods();

        String supportedMethods = (methods == null || methods.isEmpty())
                ? "No especificados"
                : methods.stream().map(HttpMethod::name).collect(Collectors.joining(", "));

        // 2) (Opcional pero recomendable) agrega el header Allow como dicta HTTP 405
        if (methods != null && !methods.isEmpty()) {
            response.setHeader("Allow", methods.stream().map(HttpMethod::name).collect(Collectors.joining(", ")));
        }

        var errorDetail = StandardResponses.errorDetail(
                "METHOD_NOT_ALLOWED",
                String.format("Método HTTP '%s' no soportado para este endpoint. Métodos soportados: %s",
                        ex.getMethod(), supportedMethods),
                null
        );

        return StandardResponses.errorResponse(
                HttpStatus.METHOD_NOT_ALLOWED.toString(),
                "Método HTTP no permitido",
                List.of(errorDetail)
        );
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public StandardResponse<Object> generic(Exception ex) {
        log.error("Unexpected error", ex);

        return StandardResponses.errorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Error interno del servidor",
                List.of()
        );
    }
}
