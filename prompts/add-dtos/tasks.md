# Task List for Adding DTOs to Beer API

## 1. Set Up Project Dependencies

- [x] Add MapStruct dependency to pom.xml

## 2. Create DTO Classes

- [x] Create package structure: `com.meh.juniemvc.api.model` for the following 3 classes
- [x] Create `BeerDto` record with validation annotations
- [x] Create `BeerCreateRequest` record with validation annotations
- [x] Create `BeerUpdateRequest` record with validation annotations

## 3. Implement MapStruct Mapper

- [x] Create package structure: `com.meh.juniemvc.mappers`
- [x] Create `BeerMapper` interface with appropriate mapping methods:
  - [x] `beerToBeerDto` method
  - [x] `beerDtoToBeer` method with ignored fields
  - [x] `beerCreateRequestToBeer` method with ignored fields
  - [x] `beerUpdateRequestToBeer` method with ignored fields

## 4. Update Service Layer

- [x] Update `BeerService` interface to use DTOs:
  - [x] Update `saveBeer` method to accept `BeerCreateRequest`
  - [x] Update `getBeerById` method to return `Optional<BeerDto>`
  - [x] Update `updateBeer` method to accept `BeerUpdateRequest`
  - [x] Update `deleteBeer` method signature if needed
- [x] Update `BeerServiceImpl` class:
  - [x] Inject `BeerMapper` dependency
  - [x] Implement updated methods using the mapper
  - [x] Add appropriate `@Transactional` annotations
  - [x] Ensure proper error handling

## 5. Update Controller Layer

- [x] Update `BeerController` class:
  - [x] Update method signatures to use DTOs
  - [x] Add `@Valid` annotations for request validation
  - [x] Return appropriate `ResponseEntity` with correct HTTP status codes
  - [x] Ensure proper error handling

## 6. Add Exception Handling

- [x] Create package structure: `com.meh.juniemvc.exceptions`
- [x] Create `ErrorResponse` record
- [x] Create `GlobalExceptionHandler` class:
  - [x] Add handler for validation exceptions
  - [x] Add handler for entity not found exceptions
  - [x] Add handler for generic exceptions

## 7. Update Tests

### 7.1 Update BeerControllerTest

- [x] Modify test methods to use DTOs instead of entities
- [x] Add tests for validation errors
- [x] Ensure all existing test cases still pass with the new DTO-based approach

### 7.2 Update BeerServiceImplTest

- [x] Modify test methods to use DTOs
- [x] Verify that the mapper is correctly converting between DTOs and entities
- [x] Test transaction boundaries

## 8. Final Verification

- [x] Run all tests to verify the implementation
- [x] Perform manual testing with tools like Postman or curl
- [x] Verify that all requirements have been met
- [x] Review code for adherence to best practices:
  - [x] Constructor injection pattern for all dependencies
  - [x] Package-private visibility where possible
  - [x] Appropriate JavaDoc comments
  - [x] REST API design principles
  - [x] Proper transaction boundaries
  - [x] Validation on all incoming requests