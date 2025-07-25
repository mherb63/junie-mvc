package com.meh.juniemvc.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for Customer entity.
 * Used for transferring customer data between the API and service layers.
 */
public record CustomerDto(
    Integer id,
    Integer version,
    
    @NotBlank(message = "Customer name is required")
    String name,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,
    
    String phone
) {}