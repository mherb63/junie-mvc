# Implementation Plan for Adding DTOs to Beer API

## 1. Set Up Project Dependencies

- Add MapStruct dependency to pom.xml:
  

## 2. Create DTO Classes

- Create package structure: `com.meh.juniemvc.api.model`
- Create `BeerDto` record:
  ```java
  package com.meh.juniemvc.api.model;
  
  import jakarta.validation.constraints.NotBlank;
  import jakarta.validation.constraints.NotNull;
  import jakarta.validation.constraints.Positive;
  import jakarta.validation.constraints.PositiveOrZero;
  
  import java.math.BigDecimal;
  import java.time.LocalDateTime;
  
  public record BeerDto(
      Integer id,
      Integer version,
      
      @NotBlank
      String beerName,
      
      @NotBlank
      String beerStyle,
      
      @NotBlank
      String upc,
      
      @NotNull
      @PositiveOrZero
      Integer quantityOnHand,
      
      @NotNull
      @Positive
      BigDecimal price,
      
      LocalDateTime createdDate,
      LocalDateTime updateDate
  ) {}
  ```

- Create `BeerCreateRequest` record:
  ```java
  package com.meh.juniemvc.api.model;
  
  import jakarta.validation.constraints.NotBlank;
  import jakarta.validation.constraints.NotNull;
  import jakarta.validation.constraints.Positive;
  import jakarta.validation.constraints.PositiveOrZero;
  
  import java.math.BigDecimal;
  
  public record BeerCreateRequest(
      @NotBlank
      String beerName,
      
      @NotBlank
      String beerStyle,
      
      @NotBlank
      String upc,
      
      @NotNull
      @PositiveOrZero
      Integer quantityOnHand,
      
      @NotNull
      @Positive
      BigDecimal price
  ) {}
  ```

- Create `BeerUpdateRequest` record:
  ```java
  package com.meh.juniemvc.api.model;
  
  import jakarta.validation.constraints.NotBlank;
  import jakarta.validation.constraints.NotNull;
  import jakarta.validation.constraints.Positive;
  import jakarta.validation.constraints.PositiveOrZero;
  
  import java.math.BigDecimal;
  
  public record BeerUpdateRequest(
      @NotBlank
      String beerName,
      
      @NotBlank
      String beerStyle,
      
      @NotBlank
      String upc,
      
      @NotNull
      @PositiveOrZero
      Integer quantityOnHand,
      
      @NotNull
      @Positive
      BigDecimal price
  ) {}
  ```

## 3. Implement MapStruct Mapper

- Create package structure: `com.meh.juniemvc.mappers`
- Create `BeerMapper` interface:
  ```java
  package com.meh.juniemvc.mappers;
  
  import com.meh.juniemvc.api.model.BeerCreateRequest;
  import com.meh.juniemvc.api.model.BeerDto;
  import com.meh.juniemvc.api.model.BeerUpdateRequest;
  import com.meh.juniemvc.domain.Beer;
  import org.mapstruct.Mapper;
  import org.mapstruct.Mapping;
  
  @Mapper(componentModel = "spring")
  public interface BeerMapper {
      BeerDto beerToBeerDto(Beer beer);
      
      @Mapping(target = "id", ignore = true)
      @Mapping(target = "createdDate", ignore = true)
      @Mapping(target = "updateDate", ignore = true)
      Beer beerDtoToBeer(BeerDto beerDto);
      
      @Mapping(target = "id", ignore = true)
      @Mapping(target = "version", ignore = true)
      @Mapping(target = "createdDate", ignore = true)
      @Mapping(target = "updateDate", ignore = true)
      Beer beerCreateRequestToBeer(BeerCreateRequest beerCreateRequest);
      
      @Mapping(target = "id", ignore = true)
      @Mapping(target = "version", ignore = true)
      @Mapping(target = "createdDate", ignore = true)
      @Mapping(target = "updateDate", ignore = true)
      Beer beerUpdateRequestToBeer(BeerUpdateRequest beerUpdateRequest);
  }
  ```

## 4. Update Service Layer

- Update `BeerService` interface:
  ```java
  package com.meh.juniemvc.services;
  
  import com.meh.juniemvc.api.model.BeerCreateRequest;
  import com.meh.juniemvc.api.model.BeerDto;
  import com.meh.juniemvc.api.model.BeerUpdateRequest;
  
  import java.util.Optional;
  
  public interface BeerService {
      BeerDto saveBeer(BeerCreateRequest beerCreateRequest);
      
      Optional<BeerDto> getBeerById(Integer id);
      
      BeerDto updateBeer(Integer id, BeerUpdateRequest beerUpdateRequest);
      
      boolean deleteBeer(Integer id);
  }
  ```

- Update `BeerServiceImpl` class:
  ```java
  package com.meh.juniemvc.services;
  
  import com.meh.juniemvc.api.model.BeerCreateRequest;
  import com.meh.juniemvc.api.model.BeerDto;
  import com.meh.juniemvc.api.model.BeerUpdateRequest;
  import com.meh.juniemvc.domain.Beer;
  import com.meh.juniemvc.mappers.BeerMapper;
  import com.meh.juniemvc.repositories.BeerRepository;
  import lombok.RequiredArgsConstructor;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;
  
  import java.util.Optional;
  
  @Service
  @RequiredArgsConstructor
  public class BeerServiceImpl implements BeerService {
  
      private final BeerRepository beerRepository;
      private final BeerMapper beerMapper;
  
      @Override
      @Transactional
      public BeerDto saveBeer(BeerCreateRequest beerCreateRequest) {
          Beer beer = beerMapper.beerCreateRequestToBeer(beerCreateRequest);
          Beer savedBeer = beerRepository.save(beer);
          return beerMapper.beerToBeerDto(savedBeer);
      }
  
      @Override
      @Transactional(readOnly = true)
      public Optional<BeerDto> getBeerById(Integer id) {
          return beerRepository.findById(id)
                  .map(beerMapper::beerToBeerDto);
      }
  
      @Override
      @Transactional
      public BeerDto updateBeer(Integer id, BeerUpdateRequest beerUpdateRequest) {
          Optional<Beer> existingBeerOptional = beerRepository.findById(id);
          
          if (existingBeerOptional.isEmpty()) {
              return null;
          }
          
          Beer existingBeer = existingBeerOptional.get();
          
          // Map properties from update request to existing beer
          Beer beerFromRequest = beerMapper.beerUpdateRequestToBeer(beerUpdateRequest);
          existingBeer.setBeerName(beerFromRequest.getBeerName());
          existingBeer.setBeerStyle(beerFromRequest.getBeerStyle());
          existingBeer.setUpc(beerFromRequest.getUpc());
          existingBeer.setPrice(beerFromRequest.getPrice());
          existingBeer.setQuantityOnHand(beerFromRequest.getQuantityOnHand());
          
          Beer savedBeer = beerRepository.save(existingBeer);
          return beerMapper.beerToBeerDto(savedBeer);
      }
  
      @Override
      @Transactional
      public boolean deleteBeer(Integer id) {
          if (!beerRepository.existsById(id)) {
              return false;
          }
          
          beerRepository.deleteById(id);
          return true;
      }
  }
  ```

## 5. Update Controller Layer

- Update `BeerController` class:
  ```java
  package com.meh.juniemvc.controllers;
  
  import com.meh.juniemvc.api.model.BeerCreateRequest;
  import com.meh.juniemvc.api.model.BeerDto;
  import com.meh.juniemvc.api.model.BeerUpdateRequest;
  import com.meh.juniemvc.services.BeerService;
  import jakarta.validation.Valid;
  import lombok.RequiredArgsConstructor;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
  import org.springframework.web.bind.annotation.*;
  
  import java.util.Optional;
  
  @RestController
  @RequestMapping("/api/v1/beer")
  @RequiredArgsConstructor
  public class BeerController {
  
      private final BeerService beerService;
  
      @PostMapping
      public ResponseEntity<BeerDto> createBeer(@Valid @RequestBody BeerCreateRequest beerCreateRequest) {
          BeerDto savedBeer = beerService.saveBeer(beerCreateRequest);
          return new ResponseEntity<>(savedBeer, HttpStatus.CREATED);
      }
  
      @PutMapping("/{beerId}")
      public ResponseEntity<BeerDto> updateBeer(@PathVariable("beerId") Integer beerId, 
                                              @Valid @RequestBody BeerUpdateRequest beerUpdateRequest) {
          BeerDto updatedBeer = beerService.updateBeer(beerId, beerUpdateRequest);
          
          if (updatedBeer == null) {
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
          
          return new ResponseEntity<>(updatedBeer, HttpStatus.OK);
      }
  
      @DeleteMapping("/{beerId}")
      public ResponseEntity<Void> deleteBeer(@PathVariable("beerId") Integer beerId) {
          boolean deleted = beerService.deleteBeer(beerId);
          
          if (!deleted) {
              return new ResponseEntity<>(HttpStatus.NOT_FOUND);
          }
          
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
  
      @GetMapping("/{beerId}")
      public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") Integer beerId) {
          Optional<BeerDto> beerOptional = beerService.getBeerById(beerId);
          
          return beerOptional
                  .map(beer -> new ResponseEntity<>(beer, HttpStatus.OK))
                  .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
      }
  }
  ```

## 6. Add Exception Handling

- Create package structure: `com.meh.juniemvc.exceptions`
- Create `ErrorResponse` class:
  ```java
  package com.meh.juniemvc.exceptions;
  
  import java.time.LocalDateTime;
  import java.util.List;
  
  public record ErrorResponse(
      LocalDateTime timestamp,
      int status,
      String error,
      String message,
      List<String> details
  ) {}
  ```

- Create `GlobalExceptionHandler` class:
  ```java
  package com.meh.juniemvc.exceptions;
  
  import jakarta.persistence.EntityNotFoundException;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
  import org.springframework.validation.FieldError;
  import org.springframework.web.bind.MethodArgumentNotValidException;
  import org.springframework.web.bind.annotation.ExceptionHandler;
  import org.springframework.web.bind.annotation.RestControllerAdvice;
  
  import java.time.LocalDateTime;
  import java.util.List;
  import java.util.stream.Collectors;
  
  @RestControllerAdvice
  class GlobalExceptionHandler {
  
      @ExceptionHandler(MethodArgumentNotValidException.class)
      public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
          List<String> errors = ex.getBindingResult()
                  .getFieldErrors()
                  .stream()
                  .map(FieldError::getDefaultMessage)
                  .collect(Collectors.toList());
  
          ErrorResponse errorResponse = new ErrorResponse(
                  LocalDateTime.now(),
                  HttpStatus.BAD_REQUEST.value(),
                  "Validation Error",
                  "Invalid request parameters",
                  errors
          );
  
          return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
      }
  
      @ExceptionHandler(EntityNotFoundException.class)
      public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
          ErrorResponse errorResponse = new ErrorResponse(
                  LocalDateTime.now(),
                  HttpStatus.NOT_FOUND.value(),
                  "Not Found",
                  ex.getMessage(),
                  List.of()
          );
  
          return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
      }
  
      @ExceptionHandler(Exception.class)
      public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
          ErrorResponse errorResponse = new ErrorResponse(
                  LocalDateTime.now(),
                  HttpStatus.INTERNAL_SERVER_ERROR.value(),
                  "Internal Server Error",
                  "An unexpected error occurred",
                  List.of(ex.getMessage())
          );
  
          return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  ```

## 7. Update Tests

### 7.1 Update BeerControllerTest

- Modify test methods to use DTOs instead of entities
- Add tests for validation errors
- Ensure all existing test cases still pass with the new DTO-based approach

### 7.2 Update BeerServiceImplTest

- Modify test methods to use DTOs
- Verify that the mapper is correctly converting between DTOs and entities
- Test transaction boundaries

## 8. Implementation Steps

1. Add MapStruct dependency to pom.xml
2. Create DTO classes in the new package
3. Implement the MapStruct mapper interface
4. Update the service layer to use DTOs and add transaction annotations
5. Update the controller layer to use DTOs and add validation
6. Implement the global exception handler
7. Update tests to work with DTOs
8. Run tests to verify the implementation
9. Manual testing with tools like Postman or curl

## 9. Considerations and Best Practices

- Follow constructor injection pattern for all dependencies
- Use package-private visibility for components where possible
- Add appropriate JavaDoc comments to explain the purpose of each class and method
- Follow REST API design principles for consistent URL patterns and HTTP status codes
- Apply proper transaction boundaries in the service layer
- Ensure validation is applied to all incoming requests
- Use MapStruct for efficient, type-safe mapping between DTOs and entities