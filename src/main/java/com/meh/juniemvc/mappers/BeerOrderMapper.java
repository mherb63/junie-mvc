package com.meh.juniemvc.mappers;

import com.meh.juniemvc.api.model.BeerOrderDto;
import com.meh.juniemvc.api.model.BeerOrderLineDto;
import com.meh.juniemvc.domain.BeerOrder;
import com.meh.juniemvc.domain.BeerOrderLine;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between BeerOrder/BeerOrderLine entities and their DTOs.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, BeerMapper.class})
public interface BeerOrderMapper {
    
    /**
     * Converts a BeerOrder entity to a BeerOrderDto.
     *
     * @param beerOrder the BeerOrder entity to convert
     * @return the corresponding BeerOrderDto
     */
    BeerOrderDto beerOrderToBeerOrderDto(BeerOrder beerOrder);
    
    /**
     * Converts a BeerOrderDto to a BeerOrder entity.
     *
     * @param beerOrderDto the BeerOrderDto to convert
     * @return the corresponding BeerOrder entity
     */
    BeerOrder beerOrderDtoToBeerOrder(BeerOrderDto beerOrderDto);
    
    /**
     * Converts a BeerOrderLine entity to a BeerOrderLineDto.
     *
     * @param beerOrderLine the BeerOrderLine entity to convert
     * @return the corresponding BeerOrderLineDto
     */
    BeerOrderLineDto beerOrderLineToBeerOrderLineDto(BeerOrderLine beerOrderLine);
    
    /**
     * Converts a BeerOrderLineDto to a BeerOrderLine entity.
     *
     * @param beerOrderLineDto the BeerOrderLineDto to convert
     * @return the corresponding BeerOrderLine entity
     */
    BeerOrderLine beerOrderLineDtoToBeerOrderLine(BeerOrderLineDto beerOrderLineDto);
}