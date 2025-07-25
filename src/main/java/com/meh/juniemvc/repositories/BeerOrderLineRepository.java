package com.meh.juniemvc.repositories;

import com.meh.juniemvc.domain.BeerOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for BeerOrderLine entity.
 * Provides CRUD operations for BeerOrderLine entities.
 */
public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, Integer> {
}