package com.meh.juniemvc.api.model;

import com.meh.juniemvc.domain.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for BeerOrder entity.
 * Used for transferring beer order data between the API and service layers.
 */
public record BeerOrderDto(
    Integer id,
    Integer version,
    LocalDateTime createdDate,
    LocalDateTime updateDate,
    
    String customerRef,
    
    @NotNull(message = "Order status is required")
    OrderStatus orderStatus,
    
    @NotNull(message = "Customer is required")
    CustomerDto customer,
    
    Set<BeerOrderLineDto> beerOrderLines
) {}