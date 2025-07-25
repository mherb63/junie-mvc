package com.meh.juniemvc.mappers;

import com.meh.juniemvc.domain.Beer;
import com.meh.juniemvc.domain.BeerCreateRequest;
import com.meh.juniemvc.domain.BeerDto;
import com.meh.juniemvc.domain.BeerUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Beer entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface BeerMapper {
    /**
     * Converts a Beer entity to a BeerDto.
     *
     * @param beer the Beer entity to convert
     * @return the converted BeerDto
     */
    BeerDto beerToBeerDto(Beer beer);
    
    /**
     * Converts a BeerDto to a Beer entity.
     * Ignores id, createdDate, and updateDate fields.
     *
     * @param beerDto the BeerDto to convert
     * @return the converted Beer entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Beer beerDtoToBeer(BeerDto beerDto);
    
    /**
     * Converts a BeerCreateRequest to a Beer entity.
     * Ignores id, version, createdDate, and updateDate fields.
     *
     * @param beerCreateRequest the BeerCreateRequest to convert
     * @return the converted Beer entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Beer beerCreateRequestToBeer(BeerCreateRequest beerCreateRequest);
    
    /**
     * Converts a BeerUpdateRequest to a Beer entity.
     * Ignores id, version, createdDate, and updateDate fields.
     *
     * @param beerUpdateRequest the BeerUpdateRequest to convert
     * @return the converted Beer entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Beer beerUpdateRequestToBeer(BeerUpdateRequest beerUpdateRequest);
}