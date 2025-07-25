package com.meh.juniemvc.domain;

/**
 * Enum representing the possible states of a beer order.
 * <p>
 * NEW - Order has been created but not processed
 * READY - Order has been processed and is ready for pickup
 * PICKED_UP - Order has been picked up by delivery service
 * DELIVERED - Order has been delivered to the customer
 * CANCELLED - Order has been cancelled
 * </p>
 */
public enum OrderStatus {
    NEW, 
    READY, 
    PICKED_UP, 
    DELIVERED, 
    CANCELLED
}