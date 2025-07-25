package com.meh.juniemvc.api.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO for BeerOrderLine entity.
 * Used for transferring beer order line data between the API and service layers.
 */
public record BeerOrderLineDto(
    Integer id,
    Integer version,
    
    @NotNull(message = "Beer is required")
    BeerDto beer,
    
    @NotNull(message = "Order quantity is required")
    @Positive(message = "Order quantity must be greater than zero")
    Integer orderQuantity,
    
    @NotNull(message = "Quantity allocated is required")
    @PositiveOrZero(message = "Quantity allocated must be zero or greater")
    Integer quantityAllocated
) {}