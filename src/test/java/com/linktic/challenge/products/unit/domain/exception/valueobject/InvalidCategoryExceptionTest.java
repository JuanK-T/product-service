package com.linktic.challenge.products.unit.domain.exception.valueobject;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidCategoryException;
import com.linktic.challenge.products.domain.exception.valueobject.ProductValueObjectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidCategoryExceptionTest {

    @Test
    @DisplayName("Dado una categoría inválida, cuando se crea InvalidCategoryException, entonces la excepción debe contener el mensaje correcto con la categoría")
    void givenInvalidCategory_whenCreatingException_thenShouldContainCorrectMessageWithCategory() {
        // Given
        String category = "InvalidCategory123";

        // When
        InvalidCategoryException exception = new InvalidCategoryException(category);

        // Then
        assertEquals("Invalid category: " + category, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Dado diferentes categorías inválidas, cuando se crea InvalidCategoryException, entonces cada excepción debe contener su respectiva categoría en el mensaje")
    void givenDifferentInvalidCategories_whenCreatingExceptions_thenEachShouldContainRespectiveCategoryInMessage() {
        // Given
        String category1 = "Category@With#Special$Chars";
        String category2 = "TooLongCategoryNameThatExceedsMaximumAllowedLength";
        String category3 = "123NumericCategory";

        // When
        InvalidCategoryException exception1 = new InvalidCategoryException(category1);
        InvalidCategoryException exception2 = new InvalidCategoryException(category2);
        InvalidCategoryException exception3 = new InvalidCategoryException(category3);

        // Then
        assertEquals("Invalid category: " + category1, exception1.getMessage());
        assertEquals("Invalid category: " + category2, exception2.getMessage());
        assertEquals("Invalid category: " + category3, exception3.getMessage());
    }

    @Test
    @DisplayName("Dado una categoría vacía, cuando se crea InvalidCategoryException, entonces la excepción debe manejar correctamente la categoría vacía")
    void givenEmptyCategory_whenCreatingException_thenShouldHandleEmptyCategoryCorrectly() {
        // Given
        String emptyCategory = "";

        // When
        InvalidCategoryException exception = new InvalidCategoryException(emptyCategory);

        // Then
        assertEquals("Invalid category: " + emptyCategory, exception.getMessage());
    }

    @Test
    @DisplayName("Dado una categoría nula, cuando se crea InvalidCategoryException, entonces la excepción debe manejar correctamente la categoría nula")
    void givenNullCategory_whenCreatingException_thenShouldHandleNullCategoryCorrectly() {

        // When
        InvalidCategoryException exception = new InvalidCategoryException(null);

        // Then
        assertEquals("Invalid category: null", exception.getMessage());
    }

    @Test
    @DisplayName("Dado una InvalidCategoryException, cuando se verifica la herencia, entonces debe ser instancia de ProductValueObjectException")
    void givenInvalidCategoryException_whenCheckingInheritance_thenShouldBeProductValueObjectException() {
        // Given
        String category = "TestCategory";

        // When
        InvalidCategoryException exception = new InvalidCategoryException(category);

        // Then
        assertInstanceOf(ProductValueObjectException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidCategoryException, cuando se verifica la herencia completa, entonces debe ser instancia de RuntimeException")
    void givenInvalidCategoryException_whenCheckingFullInheritance_thenShouldBeRuntimeException() {
        // Given
        String category = "TestCategory";

        // When
        InvalidCategoryException exception = new InvalidCategoryException(category);

        // Then
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Dado una InvalidCategoryException, cuando se obtiene el stack trace, entonces debe estar disponible para depuración")
    void givenInvalidCategoryException_whenGettingStackTrace_thenShouldBeAvailableForDebugging() {
        // Given
        String category = "DebugCategory";

        // When
        InvalidCategoryException exception = new InvalidCategoryException(category);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Dado categorías con espacios y caracteres especiales, cuando se crea InvalidCategoryException, entonces debe incluir todos los caracteres en el mensaje")
    void givenCategoryWithSpacesAndSpecialChars_whenCreatingException_thenShouldIncludeAllCharactersInMessage() {
        // Given
        String complexCategory = "Category With Spaces & Special Chárs";

        // When
        InvalidCategoryException exception = new InvalidCategoryException(complexCategory);

        // Then
        assertEquals("Invalid category: " + complexCategory, exception.getMessage());
    }

    @Test
    @DisplayName("Dado categorías que representan casos reales, cuando se crea InvalidCategoryException, entonces el mensaje debe ser informativo")
    void givenRealWorldCategories_whenCreatingException_thenMessageShouldBeInformative() {
        // Given
        String category1 = "Electrónicos y Tecnología";
        String category2 = "Hogar y Jardín";
        String category3 = "Ropa y Accesorios";

        // When
        InvalidCategoryException exception1 = new InvalidCategoryException(category1);
        InvalidCategoryException exception2 = new InvalidCategoryException(category2);
        InvalidCategoryException exception3 = new InvalidCategoryException(category3);

        // Then
        assertEquals("Invalid category: " + category1, exception1.getMessage());
        assertEquals("Invalid category: " + category2, exception2.getMessage());
        assertEquals("Invalid category: " + category3, exception3.getMessage());
    }
}