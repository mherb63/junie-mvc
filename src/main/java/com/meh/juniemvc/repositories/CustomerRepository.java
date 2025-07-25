package com.meh.juniemvc.repositories;

import com.meh.juniemvc.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Customer entity.
 * Provides CRUD operations for Customer entities.
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}