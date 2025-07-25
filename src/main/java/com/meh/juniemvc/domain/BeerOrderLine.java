package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing a line item in a beer order.
 * Each line item is associated with a beer and has order quantity and allocated quantity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"beerOrder"})
@Entity
public class BeerOrderLine extends BaseEntity {
    
    @ManyToOne
    private BeerOrder beerOrder;
    
    @ManyToOne
    private Beer beer;
    
    private Integer orderQuantity = 0;
    
    private Integer quantityAllocated = 0;
}