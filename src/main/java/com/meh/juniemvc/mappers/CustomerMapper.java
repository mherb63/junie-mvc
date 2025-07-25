package com.meh.juniemvc.mappers;

import com.meh.juniemvc.api.model.CustomerDto;
import com.meh.juniemvc.domain.Customer;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between Customer entity and CustomerDto.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {
    
    /**
     * Converts a Customer entity to a CustomerDto.
     *
     * @param customer the Customer entity to convert
     * @return the corresponding CustomerDto
     */
    CustomerDto customerToCustomerDto(Customer customer);
    
    /**
     * Converts a CustomerDto to a Customer entity.
     *
     * @param customerDto the CustomerDto to convert
     * @return the corresponding Customer entity
     */
    Customer customerDtoToCustomer(CustomerDto customerDto);
}