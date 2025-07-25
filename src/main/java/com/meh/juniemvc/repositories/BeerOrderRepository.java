package com.meh.juniemvc.repositories;

import com.meh.juniemvc.domain.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for BeerOrder entity.
 * Provides CRUD operations for BeerOrder entities.
 */
public interface BeerOrderRepository extends JpaRepository<BeerOrder, Integer> {
}