package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.HashSet;

/**
 * Entity representing a customer in the system.
 * Customers can place beer orders.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"beerOrders"})
@Entity
public class Customer extends BaseEntity {
    
    private String name;
    private String email;
    private String phone;
    
    @Builder.Default
    @OneToMany(mappedBy = "customer")
    private Set<BeerOrder> beerOrders = new HashSet<>();
}