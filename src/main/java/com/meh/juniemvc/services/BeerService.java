package com.meh.juniemvc.services;


import com.meh.juniemvc.domain.BeerCreateRequest;
import com.meh.juniemvc.domain.BeerDto;
import com.meh.juniemvc.domain.BeerUpdateRequest;

import java.util.Optional;

/**
 * Service interface for managing beer operations.
 */
public interface BeerService {
    /**
     * Saves a new beer.
     *
     * @param beerCreateRequest the beer creation request
     * @return the saved beer as a DTO
     */
    BeerDto saveBeer(BeerCreateRequest beerCreateRequest);
    
    /**
     * Retrieves a beer by its ID.
     *
     * @param id the beer ID
     * @return an Optional containing the beer DTO if found, or empty if not found
     */
    Optional<BeerDto> getBeerById(Integer id);
    
    /**
     * Updates an existing beer.
     *
     * @param id the beer ID
     * @param beerUpdateRequest the beer update request
     * @return the updated beer as a DTO, or null if the beer was not found
     */
    BeerDto updateBeer(Integer id, BeerUpdateRequest beerUpdateRequest);
    
    /**
     * Deletes a beer by its ID.
     *
     * @param id the beer ID
     * @return true if the beer was deleted, false if it was not found
     */
    boolean deleteBeer(Integer id);
}