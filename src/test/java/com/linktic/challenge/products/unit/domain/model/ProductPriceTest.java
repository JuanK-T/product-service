package com.linktic.challenge.products.unit.domain.model;

import com.linktic.challenge.products.domain.exception.valueobject.InvalidPriceException;
import com.linktic.challenge.products.domain.model.ProductPrice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductPriceTest {

    @Test
    @DisplayName("Dado un valor y currency válidos, cuando se crea ProductPrice, entonces debe crearse correctamente")
    void givenValidValueAndCurrency_whenCreatingProductPrice_thenShouldCreateSuccessfully() {
        // Given
        BigDecimal value = new BigDecimal("99.99");
        Currency currency = Currency.getInstance("USD");

        // When
        ProductPrice price = new ProductPrice(value, currency);

        // Then
        assertEquals(value, price.value());
        assertEquals(currency, price.currency());
    }

    @Test
    @DisplayName("Dado valor cero, cuando se crea ProductPrice, entonces debe crearse correctamente")
    void givenZeroValue_whenCreatingProductPrice_thenShouldCreateSuccessfully() {
        // Given
        BigDecimal zeroValue = BigDecimal.ZERO;
        Currency currency = Currency.getInstance("EUR");

        // When
        ProductPrice price = new ProductPrice(zeroValue, currency);

        // Then
        assertEquals(zeroValue, price.value());
        assertEquals(currency, price.currency());
    }

    @Test
    @DisplayName("Dado valor positivo grande, cuando se crea ProductPrice, entonces debe crearse correctamente")
    void givenLargePositiveValue_whenCreatingProductPrice_thenShouldCreateSuccessfully() {
        // Given
        BigDecimal largeValue = new BigDecimal("999999.99");
        Currency currency = Currency.getInstance("JPY");

        // When
        ProductPrice price = new ProductPrice(largeValue, currency);

        // Then
        assertEquals(largeValue, price.value());
        assertEquals(currency, price.currency());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Dado valor nulo, cuando se crea ProductPrice, entonces debe lanzar InvalidPriceException")
    void givenNullValue_whenCreatingProductPrice_thenShouldThrowInvalidPriceException(BigDecimal nullValue) {
        // Given
        Currency currency = Currency.getInstance("USD");

        // When & Then
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () ->
                new ProductPrice(nullValue, currency)
        );

        assertEquals("Price value cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "-1.00", "-100.50", "-999.99"})
    @DisplayName("Dado valores negativos, cuando se crea ProductPrice, entonces debe lanzar InvalidPriceException")
    void givenNegativeValues_whenCreatingProductPrice_thenShouldThrowInvalidPriceException(String negativeValue) {
        // Given
        BigDecimal value = new BigDecimal(negativeValue);
        Currency currency = Currency.getInstance("USD");

        // When & Then
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () ->
                new ProductPrice(value, currency)
        );

        assertEquals("Price must be non-negative", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Dado currency nulo, cuando se crea ProductPrice, entonces debe lanzar InvalidPriceException")
    void givenNullCurrency_whenCreatingProductPrice_thenShouldThrowInvalidPriceException(Currency nullCurrency) {
        // Given
        BigDecimal value = new BigDecimal("50.00");

        // When & Then
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () ->
                new ProductPrice(value, nullCurrency)
        );

        assertEquals("Price currency cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Dado parámetros válidos, cuando se usa factory method of(), entonces debe crear el mismo ProductPrice")
    void givenValidParameters_whenUsingFactoryMethod_thenShouldCreateSameProductPrice() {
        // Given
        BigDecimal value = new BigDecimal("75.50");
        Currency currency = Currency.getInstance("EUR");

        // When
        ProductPrice fromConstructor = new ProductPrice(value, currency);
        ProductPrice fromFactory = ProductPrice.of(value, currency);

        // Then
        assertEquals(fromConstructor.value(), fromFactory.value());
        assertEquals(fromConstructor.currency(), fromFactory.currency());
        assertEquals(value, fromFactory.value());
        assertEquals(currency, fromFactory.currency());
    }

    @Test
    @DisplayName("Dado parámetros inválidos, cuando se usa factory method of(), entonces debe lanzar la misma excepción")
    void givenInvalidParameters_whenUsingFactoryMethod_thenShouldThrowSameException() {
        // Given
        BigDecimal negativeValue = new BigDecimal("-10.00");
        Currency currency = Currency.getInstance("USD");

        // When & Then
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () ->
                ProductPrice.of(negativeValue, currency)
        );

        assertEquals("Price must be non-negative", exception.getMessage());
    }

    @Test
    @DisplayName("Dado dos ProductPrice con misma moneda, cuando se suman, entonces debe retornar suma correcta")
    void givenTwoProductPricesWithSameCurrency_whenAdding_thenShouldReturnCorrectSum() {
        // Given
        ProductPrice price1 = new ProductPrice(new BigDecimal("25.50"), Currency.getInstance("USD"));
        ProductPrice price2 = new ProductPrice(new BigDecimal("15.25"), Currency.getInstance("USD"));

        // When
        ProductPrice result = price1.add(price2);

        // Then
        assertEquals(new BigDecimal("40.75"), result.value());
        assertEquals(Currency.getInstance("USD"), result.currency());
    }

    @Test
    @DisplayName("Dado dos ProductPrice con diferentes monedas, cuando se suman, entonces debe lanzar InvalidPriceException")
    void givenTwoProductPricesWithDifferentCurrencies_whenAdding_thenShouldThrowInvalidPriceException() {
        // Given
        ProductPrice price1 = new ProductPrice(new BigDecimal("25.50"), Currency.getInstance("USD"));
        ProductPrice price2 = new ProductPrice(new BigDecimal("15.25"), Currency.getInstance("EUR"));

        // When & Then
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () ->
                price1.add(price2)
        );

        assertEquals("Price cannot add prices with different currencies", exception.getMessage());
    }

    @Test
    @DisplayName("Dado suma con cero, cuando se suman precios, entonces debe retornar el mismo precio")
    void givenZeroPrice_whenAdding_thenShouldReturnSamePrice() {
        // Given
        ProductPrice price = new ProductPrice(new BigDecimal("30.00"), Currency.getInstance("USD"));
        ProductPrice zeroPrice = new ProductPrice(BigDecimal.ZERO, Currency.getInstance("USD"));

        // When
        ProductPrice result = price.add(zeroPrice);

        // Then
        assertEquals(price.value(), result.value());
        assertEquals(price.currency(), result.currency());
    }

    @Test
    @DisplayName("Dado dos ProductPrice con mismos valores, cuando se comparan, entonces deben ser iguales")
    void givenTwoProductPricesWithSameValues_whenComparing_thenShouldBeEqual() {
        // Given
        BigDecimal value = new BigDecimal("45.99");
        Currency currency = Currency.getInstance("EUR");
        ProductPrice price1 = new ProductPrice(value, currency);
        ProductPrice price2 = new ProductPrice(value, currency);

        // Then
        assertEquals(price1, price2);
        assertEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    @DisplayName("Dado dos ProductPrice con diferentes valores, cuando se comparan, entonces no deben ser iguales")
    void givenTwoProductPricesWithDifferentValues_whenComparing_thenShouldNotBeEqual() {
        // Given
        Currency currency = Currency.getInstance("USD");
        ProductPrice price1 = new ProductPrice(new BigDecimal("20.00"), currency);
        ProductPrice price2 = new ProductPrice(new BigDecimal("30.00"), currency);

        // Then
        assertNotEquals(price1, price2);
    }

    @Test
    @DisplayName("Dado dos ProductPrice con diferentes monedas, cuando se comparan, entonces no deben ser iguales")
    void givenTwoProductPricesWithDifferentCurrencies_whenComparing_thenShouldNotBeEqual() {
        // Given
        BigDecimal value = new BigDecimal("50.00");
        ProductPrice price1 = new ProductPrice(value, Currency.getInstance("USD"));
        ProductPrice price2 = new ProductPrice(value, Currency.getInstance("EUR"));

        // Then
        assertNotEquals(price1, price2);
    }

    @Test
    @DisplayName("Dado un ProductPrice, cuando se obtiene su representación string, entonces debe incluir valor y currency")
    void givenProductPrice_whenCallingToString_thenShouldIncludeValueAndCurrency() {
        // Given
        BigDecimal value = new BigDecimal("99.50");
        Currency currency = Currency.getInstance("USD");
        ProductPrice price = new ProductPrice(value, currency);

        // When
        String stringRepresentation = price.toString();

        // Then
        assertTrue(stringRepresentation.contains(value.toString()));
        assertTrue(stringRepresentation.contains(currency.toString()));
    }

    @ParameterizedTest
    @MethodSource("validPriceValuesProvider")
    @DisplayName("Dado diferentes valores válidos, cuando se crea ProductPrice, entonces deben crearse correctamente")
    void givenDifferentValidValues_whenCreatingProductPrice_thenShouldCreateSuccessfully(BigDecimal value, Currency currency) {
        // When & Then
        assertDoesNotThrow(() -> {
            ProductPrice price = new ProductPrice(value, currency);
            assertEquals(value, price.value());
            assertEquals(currency, price.currency());
        });
    }

    static Stream<Arguments> validPriceValuesProvider() {
        return Stream.of(
                Arguments.of(new BigDecimal("0.00"), Currency.getInstance("USD")),
                Arguments.of(new BigDecimal("0.01"), Currency.getInstance("EUR")),
                Arguments.of(new BigDecimal("1.00"), Currency.getInstance("JPY")),
                Arguments.of(new BigDecimal("999.99"), Currency.getInstance("GBP")),
                Arguments.of(new BigDecimal("1000000.00"), Currency.getInstance("CAD"))
        );
    }

    @Test
    @DisplayName("Dado suma de múltiples precios con misma moneda, cuando se suman secuencialmente, entonces debe retornar suma acumulada correcta")
    void givenMultipleProductPricesWithSameCurrency_whenAddingSequentially_thenShouldReturnCorrectAccumulatedSum() {
        // Given
        ProductPrice price1 = new ProductPrice(new BigDecimal("10.00"), Currency.getInstance("USD"));
        ProductPrice price2 = new ProductPrice(new BigDecimal("20.00"), Currency.getInstance("USD"));
        ProductPrice price3 = new ProductPrice(new BigDecimal("30.00"), Currency.getInstance("USD"));

        // When
        ProductPrice intermediateSum = price1.add(price2);
        ProductPrice finalSum = intermediateSum.add(price3);

        // Then
        assertEquals(new BigDecimal("60.00"), finalSum.value());
        assertEquals(Currency.getInstance("USD"), finalSum.currency());
    }

    @Test
    @DisplayName("Dado operación de suma, cuando se verifica inmutabilidad, entonces los objetos originales no deben modificarse")
    void givenAddOperation_whenCheckingImmutability_thenOriginalObjectsShouldNotChange() {
        // Given
        ProductPrice original1 = new ProductPrice(new BigDecimal("15.00"), Currency.getInstance("USD"));
        ProductPrice original2 = new ProductPrice(new BigDecimal("25.00"), Currency.getInstance("USD"));
        BigDecimal originalValue1 = original1.value();
        BigDecimal originalValue2 = original2.value();

        // When
        ProductPrice result = original1.add(original2);

        // Then
        assertEquals(originalValue1, original1.value());
        assertEquals(originalValue2, original2.value());
        assertEquals(new BigDecimal("40.00"), result.value());
    }
}
