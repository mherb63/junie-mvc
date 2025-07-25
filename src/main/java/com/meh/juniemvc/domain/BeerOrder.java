package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.HashSet;

/**
 * Entity representing a beer order in the system.
 * A beer order is placed by a customer and contains one or more beer order lines.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"beerOrderLines"})
@Entity
public class BeerOrder extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.NEW;
    
    @ManyToOne
    private Customer customer;
    
    @Builder.Default
    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL)
    private Set<BeerOrderLine> beerOrderLines = new HashSet<>();
    
    /**
     * Helper method to add beer order line and maintain bidirectional relationship
     */
    public void addBeerOrderLine(BeerOrderLine beerOrderLine) {
        if (beerOrderLines == null) {
            beerOrderLines = new HashSet<>();
        }
        
        beerOrderLines.add(beerOrderLine);
        beerOrderLine.setBeerOrder(this);
    }
}