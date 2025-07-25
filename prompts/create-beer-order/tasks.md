# Beer Order System Implementation Tasks

## Domain Model Entities

1. [x] Create BaseEntity abstract class
   - [x] Add common fields (id, version, createdDate, updateDate)
   - [x] Implement @PrePersist and @PreUpdate methods
   - [x] Apply appropriate Lombok annotations (@Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor, @SuperBuilder)
   - [x] Add @MappedSuperclass annotation

2. [x] Refactor Beer entity to extend BaseEntity
   - [x] Remove duplicate fields and methods now in BaseEntity
   - [x] Replace @Data with @Getter, @Setter, and @ToString
   - [x] Replace @Builder with @SuperBuilder
   - [x] Add @ToString(callSuper = true)

3. [x] Create OrderStatus enum
   - [x] Define states: NEW, READY, PICKED_UP, DELIVERED, CANCELLED

4. [x] Create Customer entity
   - [x] Extend BaseEntity
   - [x] Add fields: name, email, phone
   - [x] Set up one-to-many relationship with BeerOrder
   - [x] Apply appropriate Lombok annotations
   - [x] Add @ToString.Exclude for beerOrders collection

5. [x] Create BeerOrder entity
   - [x] Extend BaseEntity
   - [x] Add fields: customerRef, orderStatus
   - [x] Set up many-to-one relationship with Customer
   - [x] Set up one-to-many relationship with BeerOrderLine
   - [x] Implement helper method to add BeerOrderLine
   - [x] Apply appropriate Lombok annotations
   - [x] Add @ToString.Exclude for beerOrderLines collection

6. [x] Create BeerOrderLine entity
   - [x] Extend BaseEntity
   - [x] Add fields: orderQuantity, quantityAllocated
   - [x] Set up many-to-one relationship with BeerOrder
   - [x] Set up many-to-one relationship with Beer
   - [x] Apply appropriate Lombok annotations
   - [x] Add @ToString.Exclude for beerOrder

## Repositories

7. [x] Create CustomerRepository interface
   - [x] Extend JpaRepository with Customer entity and Integer ID

8. [x] Create BeerOrderRepository interface
   - [x] Extend JpaRepository with BeerOrder entity and Integer ID

9. [x] Create BeerOrderLineRepository interface
   - [x] Extend JpaRepository with BeerOrderLine entity and Integer ID

## DTOs

10. [x] Create CustomerDto record
    - [x] Include fields: id, version, name, email, phone
    - [x] Add validation annotations

11. [x] Create BeerOrderDto record
    - [x] Include fields: id, version, createdDate, updateDate, customerRef, orderStatus, customer, beerOrderLines
    - [x] Add validation annotations

12. [x] Create BeerOrderLineDto record
    - [x] Include fields: id, version, beer, orderQuantity, quantityAllocated
    - [x] Add validation annotations

## Mappers

13. [x] Create CustomerMapper interface
    - [x] Define methods to convert between Customer and CustomerDto
    - [x] Add MapStruct annotations

14. [x] Create BeerOrderMapper interface
    - [x] Define methods to convert between BeerOrder and BeerOrderDto
    - [x] Define methods to convert between BeerOrderLine and BeerOrderLineDto
    - [x] Add MapStruct annotations with dependencies on CustomerMapper and BeerMapper

## Services

15. [ ] Create BeerOrderService interface
    - [ ] Define methods: createBeerOrder, getBeerOrderById, getAllBeerOrders, getBeerOrdersByCustomerId, updateBeerOrderStatus

16. [ ] Implement BeerOrderServiceImpl class
    - [ ] Use constructor injection for repositories and mappers
    - [ ] Implement all methods from the interface
    - [ ] Add appropriate @Transactional annotations
    - [ ] Add @Transactional(readOnly = true) for query methods

17. [ ] Create CustomerService interface
    - [ ] Define methods: createCustomer, getCustomerById, getAllCustomers, updateCustomer, deleteCustomer

18. [ ] Implement CustomerServiceImpl class
    - [ ] Use constructor injection for repository and mapper
    - [ ] Implement all methods from the interface
    - [ ] Add appropriate @Transactional annotations
    - [ ] Add @Transactional(readOnly = true) for query methods

## Controllers

19. [ ] Create BeerOrderController
    - [ ] Use constructor injection for BeerOrderService
    - [ ] Implement endpoints: POST /api/v1/orders, GET /api/v1/orders/{orderId}, GET /api/v1/orders, GET /api/v1/orders/customer/{customerId}, PATCH /api/v1/orders/{orderId}/status
    - [ ] Add appropriate ResponseEntity return types
    - [ ] Add validation for request bodies

20. [ ] Create CustomerController
    - [ ] Use constructor injection for CustomerService
    - [ ] Implement endpoints: POST /api/v1/customers, GET /api/v1/customers/{customerId}, GET /api/v1/customers, PUT /api/v1/customers/{customerId}, DELETE /api/v1/customers/{customerId}
    - [ ] Add appropriate ResponseEntity return types
    - [ ] Add validation for request bodies

## Configuration

21. [ ] Update application.properties
    - [ ] Set spring.jpa.open-in-view=false

## Tests

22. [ ] Write repository tests
    - [ ] CustomerRepositoryTest with Testcontainers
    - [ ] BeerOrderRepositoryTest with Testcontainers
    - [ ] BeerOrderLineRepositoryTest with Testcontainers

23. [ ] Write service tests
    - [ ] CustomerServiceImplTest with mocked repositories
    - [ ] BeerOrderServiceImplTest with mocked repositories

24. [ ] Write controller tests
    - [ ] CustomerControllerTest with MockMvc
    - [ ] BeerOrderControllerTest with MockMvc

25. [ ] Write integration tests
    - [ ] CustomerIntegrationTest with @SpringBootTest
    - [ ] BeerOrderIntegrationTest with @SpringBootTest

## Documentation

26. [ ] Add Javadoc comments to all public classes and methods
    - [ ] Document entity classes
    - [ ] Document service interfaces and implementations
    - [ ] Document controller endpoints

## Final Steps

27. [ ] Review code for adherence to Spring Boot best practices
    - [ ] Verify constructor injection is used consistently
    - [ ] Check transaction boundaries
    - [ ] Ensure proper entity relationship management

28. [ ] Run all tests to verify implementation
    - [ ] Fix any failing tests
    - [ ] Ensure test coverage is adequate

29. [ ] Perform manual testing of API endpoints
    - [ ] Test customer creation and retrieval
    - [ ] Test order creation and status updates
    - [ ] Verify error handling