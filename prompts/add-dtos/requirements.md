# Requirements for Adding DTOs to Beer API

## Background and Rationale

The current implementation directly exposes JPA entities in the REST API, which violates the "Separate Web Layer from Persistence Layer" guideline. This tightly couples the API contract to the database schema, making it difficult to evolve the API and database independently. Additionally, there's no validation for incoming requests, and the service layer doesn't have clear transaction boundaries.

By introducing DTOs (Data Transfer Objects), we can:
1. Decouple the API contract from the database schema
2. Add validation for incoming requests
3. Control exactly which fields are exposed to clients
4. Simplify mapping between API requests/responses and domain entities
5. Improve maintainability and flexibility of the codebase
6. Protect sensitive data by excluding it from API responses
7. Enable independent evolution of the API and database schemas

## Requirements

### 1. Create DTO Classes

1. Create a `BeerDto` record in a new package `com.meh.juniemvc.api.model` with the following properties:
   - Integer id
   - Integer version
   - String beerName
   - String beerStyle
   - String upc
   - Integer quantityOnHand
   - BigDecimal price
   - LocalDateTime createdDate
   - LocalDateTime updateDate

2. Add appropriate Jakarta Validation annotations to the `BeerDto` record:
   - `@NotBlank` for beerName, beerStyle, and upc
   - `@NotNull` for price and quantityOnHand
   - `@Positive` for price
   - `@PositiveOrZero` for quantityOnHand

3. Create a `BeerCreateRequest` record for beer creation operations with the same properties as `BeerDto` except id, version, createdDate, and updateDate.

4. Create a `BeerUpdateRequest` record for beer update operations with the same properties as `BeerCreateRequest`.

### 2. Implement MapStruct Mapper

1. Add MapStruct dependency to the project if not already present.

2. Create a `BeerMapper` interface in a new package `com.meh.juniemvc.mappers` with the following methods:
   - `BeerDto beerToBeerDto(Beer beer)`
   - `Beer beerDtoToBeer(BeerDto beerDto)`
   - `Beer beerCreateRequestToBeer(BeerCreateRequest beerCreateRequest)`
   - `Beer beerUpdateRequestToBeer(BeerUpdateRequest beerUpdateRequest)`

3. Configure the mapper to ignore id, createdDate, and updateDate properties when mapping from DTOs to entities.

### 3. Update Service Layer

1. Update the `BeerService` interface to use DTOs:
   - `BeerDto saveBeer(BeerCreateRequest beerCreateRequest)`
   - `Optional<BeerDto> getBeerById(Integer id)`
   - `BeerDto updateBeer(Integer id, BeerUpdateRequest beerUpdateRequest)`
   - `boolean deleteBeer(Integer id)`

2. Update the `BeerServiceImpl` class to implement the updated interface:
   - Inject the `BeerMapper` using constructor injection
   - Use the mapper to convert between DTOs and entities
   - Add appropriate transaction annotations:
     - `@Transactional(readOnly = true)` for query methods
     - `@Transactional` for data-modifying methods

### 4. Update Controller Layer

1. Update the `BeerController` to use DTOs:
   - `ResponseEntity<BeerDto> createBeer(@Valid @RequestBody BeerCreateRequest beerCreateRequest)`
   - `ResponseEntity<BeerDto> updateBeer(@PathVariable("beerId") Integer beerId, @Valid @RequestBody BeerUpdateRequest beerUpdateRequest)`
   - `ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") Integer beerId)`

2. Add validation for incoming requests using the `@Valid` annotation.

3. Ensure appropriate HTTP status codes are returned:
   - 201 Created for successful creation
   - 200 OK for successful retrieval and update
   - 204 No Content for successful deletion
   - 404 Not Found when a resource doesn't exist
   - 400 Bad Request for validation errors

### 5. Add Exception Handling

1. Create a global exception handler using `@RestControllerAdvice` to handle common exceptions:
   - `MethodArgumentNotValidException` for validation errors
   - `EntityNotFoundException` for when a resource is not found
   - Other relevant exceptions

2. Return consistent error responses with appropriate HTTP status codes and error messages.

### 6. Update Tests

1. Update `BeerControllerTest` to:
   - Use DTOs instead of entities in test setup and assertions
   - Add tests for validation errors (e.g., missing required fields)
   - Ensure all existing test cases still pass with the new DTO-based approach

2. Update `BeerServiceImplTest` to:
   - Use DTOs in test methods
   - Verify that the mapper is correctly converting between DTOs and entities
   - Test transaction boundaries

## Implementation Guidelines

1. Follow the constructor injection pattern for all dependencies.
2. Use package-private visibility for components where possible.
3. Add appropriate JavaDoc comments to explain the purpose of each class and method.
4. Follow REST API design principles for consistent URL patterns and HTTP status codes.
5. Apply proper transaction boundaries in the service layer.
6. Ensure validation is applied to all incoming requests.
7. Use MapStruct for efficient, type-safe mapping between DTOs and entities.