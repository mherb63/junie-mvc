package com.meh.juniemvc.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * DTO for updating an existing Beer.
 * Contains only the fields that can be updated.
 */
public record BeerUpdateRequest(
    @NotBlank(message = "Beer name is required")
    String beerName,
    
    @NotBlank(message = "Beer style is required")
    String beerStyle,
    
    @NotBlank(message = "UPC is required")
    String upc,
    
    @NotNull(message = "Quantity on hand is required")
    @PositiveOrZero(message = "Quantity on hand must be zero or greater")
    Integer quantityOnHand,
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    BigDecimal price
) {}