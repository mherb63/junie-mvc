### Refactoring JPA Entities with a Base Class Using @Data

Based on the previous refactoring where we created a base abstract class and used `@Getter` and `@Setter`, I'll now refactor the code to use `@Data` instead. This will simplify the code while maintaining the inheritance structure.

#### 1. Create BaseEntity Abstract Class with @Data

```java
package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
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

#### 2. Refactor Beer Entity

```java
package com.meh.juniemvc.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Beer extends BaseEntity {

    private String beerName;
    private String beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
}
```

#### 3. Refactor BeerOrder Entity

```java
package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class BeerOrder extends BaseEntity {

    private String customerRef;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    
    // Many orders belong to one customer
    @ManyToOne
    private Customer customer;
    
    // One order has many order lines
    @lombok.Builder.Default
    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL)
    private Set<BeerOrderLine> beerOrderLines = new HashSet<>();
    
    // Helper method to add beer order line and maintain relationship
    public void addBeerOrderLine(BeerOrderLine beerOrderLine) {
        if (beerOrderLines == null) {
            beerOrderLines = new HashSet<>();
        }
        
        beerOrderLines.add(beerOrderLine);
        beerOrderLine.setBeerOrder(this);
    }
}

// Order status enum (can be in a separate file)
enum OrderStatus {
    NEW, READY, PICKED_UP, DELIVERED, CANCELLED
}
```

#### 4. Refactor BeerOrderLine Entity

```java
package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class BeerOrderLine extends BaseEntity {
    
    // Many order lines belong to one order
    @ManyToOne
    private BeerOrder beerOrder;
    
    // Many order lines can reference one beer
    @ManyToOne
    private Beer beer;
    
    private Integer orderQuantity;
    
    private Integer quantityAllocated;
}
```

#### 5. Refactor Customer Entity

```java
package com.meh.juniemvc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Customer extends BaseEntity {
    
    private String name;
    private String email;
    private String phone;
    
    @lombok.Builder.Default
    @OneToMany(mappedBy = "customer")
    private Set<BeerOrder> beerOrders = new HashSet<>();
}
```

### Important Implementation Notes

1. **@Data vs @Getter/@Setter**:
    - `@Data` is a convenient shortcut annotation that bundles the features of `@ToString`, `@EqualsAndHashCode`, `@Getter`, `@Setter`, and `@RequiredArgsConstructor`
    - When using `@Data` with inheritance, Lombok will generate methods that respect the inheritance hierarchy

2. **Collection Initialization**:
    - For `@Builder.Default` with `@SuperBuilder`, use the fully qualified name `lombok.Builder.Default` to avoid ambiguity

3. **Potential Concerns with @Data in Entities**:
    - Be aware that `@Data` generates `equals()` and `hashCode()` methods based on all fields
    - For entities with bidirectional relationships, this can lead to infinite recursion in `toString()` methods
    - If this becomes an issue, consider using `@ToString(exclude = {"fieldName"})` to exclude problematic fields

4. **Lombok and JPA**:
    - Lombok works well with JPA, but be cautious with bidirectional relationships
    - The generated `equals()` and `hashCode()` methods might cause issues with lazy loading
    - If needed, you can override these methods or use `@EqualsAndHashCode(exclude = {"fieldName"})` to exclude certain fields

This refactoring maintains all the benefits of the previous approach while simplifying the code by using `@Data` instead of separate `@Getter` and `@Setter` annotations. The inheritance structure with `BaseEntity` still eliminates code duplication for the common audit fields and methods.