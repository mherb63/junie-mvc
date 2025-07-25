package com.meh.juniemvc.services;

import com.meh.juniemvc.domain.Beer;
import com.meh.juniemvc.domain.BeerCreateRequest;
import com.meh.juniemvc.domain.BeerDto;
import com.meh.juniemvc.domain.BeerUpdateRequest;
import com.meh.juniemvc.mappers.BeerMapper;
import com.meh.juniemvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of the BeerService interface.
 */
@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    @Transactional
    public BeerDto saveBeer(BeerCreateRequest beerCreateRequest) {
        Beer beer = beerMapper.beerCreateRequestToBeer(beerCreateRequest);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerDto> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    @Transactional
    public BeerDto updateBeer(Integer id, BeerUpdateRequest beerUpdateRequest) {
        Optional<Beer> existingBeerOptional = beerRepository.findById(id);
        
        if (existingBeerOptional.isEmpty()) {
            return null;
        }
        
        Beer existingBeer = existingBeerOptional.get();
        
        // Map properties from update request to existing beer
        Beer beerFromRequest = beerMapper.beerUpdateRequestToBeer(beerUpdateRequest);
        existingBeer.setBeerName(beerFromRequest.getBeerName());
        existingBeer.setBeerStyle(beerFromRequest.getBeerStyle());
        existingBeer.setUpc(beerFromRequest.getUpc());
        existingBeer.setPrice(beerFromRequest.getPrice());
        existingBeer.setQuantityOnHand(beerFromRequest.getQuantityOnHand());
        
        Beer savedBeer = beerRepository.save(existingBeer);
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    @Transactional
    public boolean deleteBeer(Integer id) {
        if (!beerRepository.existsById(id)) {
            return false;
        }
        
        beerRepository.deleteById(id);
        return true;
    }
}