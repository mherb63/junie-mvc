### Requirements for Beer Order System Implementation

#### Overview
This document outlines the requirements for implementing a beer ordering system with JPA entities following Spring Boot best practices. The implementation will include creating a base entity class and several domain entities with appropriate relationships.

#### Domain Model Entities

##### 1. BaseEntity Abstract Class
Create an abstract base class to eliminate code duplication for common fields and methods across entities.

```java
package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    @Column(updatable = false)
    private LocalDateTime createdDate;
    
    private LocalDateTime updateDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
```

##### 2. Beer Entity
Refactor the existing Beer entity to extend BaseEntity.

```java
package com.meh.juniemvc.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Entity
public class Beer extends BaseEntity {

    private String beerName;
    private String beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
}
```

##### 3. Customer Entity
Create a Customer entity to represent beer customers.

```java
package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.HashSet;

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
```

##### 4. OrderStatus Enum
Create an enum to represent the possible states of a beer order.

```java
package com.meh.juniemvc.domain;

public enum OrderStatus {
    NEW, READY, PICKED_UP, DELIVERED, CANCELLED
}
```

##### 5. BeerOrder Entity
Create a BeerOrder entity to represent customer orders.

```java
package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"beerOrderLines"})
@Entity
public class BeerOrder extends BaseEntity {

    private String customerRef;
    
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
```

##### 6. BeerOrderLine Entity
Create a BeerOrderLine entity to represent individual line items in an order.

```java
package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
```

#### Implementation Guidelines

##### 1. Prefer Constructor Injection
When creating services that use these entities, use constructor injection for repositories and other dependencies.

```java
@Service
public class BeerOrderService {
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;

    public BeerOrderService(BeerOrderRepository beerOrderRepository, 
                           CustomerRepository customerRepository) {
        this.beerOrderRepository = beerOrderRepository;
        this.customerRepository = customerRepository;
    }
    
    // Service methods
}
```

##### 2. Use @Getter/@Setter Instead of @Data
- Avoid using `@Data` for JPA entities with bidirectional relationships to prevent infinite recursion issues
- Use `@Getter`, `@Setter`, and explicit `@ToString` with appropriate exclusions instead
- Include `callSuper = true` in `@ToString` to include parent class fields

##### 3. Bidirectional Relationship Management
- Implement helper methods to maintain both sides of bidirectional relationships
- Use `@ToString.Exclude` to prevent circular references in toString() methods
- Initialize collections in default constructors or with `@Builder.Default`

##### 4. Transaction Management
- Define clear transaction boundaries in service methods
- Use `@Transactional(readOnly = true)` for query methods
- Use `@Transactional` for data-modifying methods

##### 5. Validation
- Add validation annotations to entity fields where appropriate
- Consider creating DTOs with validation annotations for controller request/response objects

##### 6. Testing
- Use Testcontainers for integration tests to test with a real database
- Write unit tests for entity relationships and helper methods

#### Potential Enhancements for Future Iterations

1. **Auditing**: Consider using Spring Data JPA's auditing capabilities instead of manual `@PrePersist` and `@PreUpdate` methods
2. **DTOs**: Create DTOs to separate the web layer from the persistence layer
3. **Pagination**: Implement pagination for collection endpoints
4. **Exception Handling**: Create a centralized exception handler
5. **Internationalization**: Externalize user-facing messages

#### Conclusion
This implementation follows Spring Boot best practices by using constructor injection, defining clear transaction boundaries, and properly managing JPA entity relationships. The use of a base entity class reduces code duplication, while the careful application of Lombok annotations improves code readability without introducing common pitfalls.