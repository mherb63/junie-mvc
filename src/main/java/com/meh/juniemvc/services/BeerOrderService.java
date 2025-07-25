package com.meh.juniemvc.services;

import com.meh.juniemvc.api.model.BeerOrderDto;
import com.meh.juniemvc.domain.OrderStatus;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for beer order operations.
 */
public interface BeerOrderService {
    
    /**
     * Creates a new beer order.
     *
     * @param beerOrderDto the beer order data
     * @return the created beer order
     */
    BeerOrderDto createBeerOrder(BeerOrderDto beerOrderDto);
    
    /**
     * Retrieves a beer order by its ID.
     *
     * @param id the beer order ID
     * @return an Optional containing the beer order if found, or empty if not found
     */
    Optional<BeerOrderDto> getBeerOrderById(Integer id);
    
    /**
     * Retrieves all beer orders.
     *
     * @return a list of all beer orders
     */
    List<BeerOrderDto> getAllBeerOrders();
    
    /**
     * Retrieves all beer orders for a specific customer.
     *
     * @param customerId the customer ID
     * @return a list of beer orders for the customer
     */
    List<BeerOrderDto> getBeerOrdersByCustomerId(Integer customerId);
    
    /**
     * Updates the status of a beer order.
     *
     * @param id the beer order ID
     * @param orderStatus the new order status
     * @return the updated beer order
     */
    BeerOrderDto updateBeerOrderStatus(Integer id, OrderStatus orderStatus);
}