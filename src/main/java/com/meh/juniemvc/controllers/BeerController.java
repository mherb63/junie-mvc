package com.meh.juniemvc.controllers;

import com.meh.juniemvc.domain.BeerCreateRequest;
import com.meh.juniemvc.domain.BeerDto;
import com.meh.juniemvc.domain.BeerUpdateRequest;
import com.meh.juniemvc.services.BeerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for managing beer operations.
 */
@RestController
@RequestMapping("/api/v1/beer")
@RequiredArgsConstructor
class BeerController {

    private final BeerService beerService;

    /**
     * Creates a new beer.
     *
     * @param beerCreateRequest the beer creation request
     * @return the created beer with status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<BeerDto> createBeer(@Valid @RequestBody BeerCreateRequest beerCreateRequest) {
        BeerDto savedBeer = beerService.saveBeer(beerCreateRequest);
        return new ResponseEntity<>(savedBeer, HttpStatus.CREATED);
    }

    /**
     * Updates an existing beer.
     *
     * @param beerId the beer ID
     * @param beerUpdateRequest the beer update request
     * @return the updated beer with status 200 (OK), or status 404 (Not Found) if the beer was not found
     */
    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> updateBeer(@PathVariable("beerId") Integer beerId, 
                                            @Valid @RequestBody BeerUpdateRequest beerUpdateRequest) {
        BeerDto updatedBeer = beerService.updateBeer(beerId, beerUpdateRequest);
        
        if (updatedBeer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(updatedBeer, HttpStatus.OK);
    }

    /**
     * Deletes a beer by its ID.
     *
     * @param beerId the beer ID
     * @return status 204 (No Content) if deleted, or status 404 (Not Found) if the beer was not found
     */
    @DeleteMapping("/{beerId}")
    public ResponseEntity<Void> deleteBeer(@PathVariable("beerId") Integer beerId) {
        boolean deleted = beerService.deleteBeer(beerId);
        
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves a beer by its ID.
     *
     * @param beerId the beer ID
     * @return the beer with status 200 (OK), or status 404 (Not Found) if the beer was not found
     */
    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") Integer beerId) {
        Optional<BeerDto> beerOptional = beerService.getBeerById(beerId);
        
        return beerOptional
                .map(beer -> new ResponseEntity<>(beer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}