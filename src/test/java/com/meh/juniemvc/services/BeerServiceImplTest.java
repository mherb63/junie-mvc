package com.meh.juniemvc.services;

import com.meh.juniemvc.domain.Beer;
import com.meh.juniemvc.domain.BeerCreateRequest;
import com.meh.juniemvc.domain.BeerDto;
import com.meh.juniemvc.domain.BeerUpdateRequest;
import com.meh.juniemvc.mappers.BeerMapper;
import com.meh.juniemvc.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {

    @Mock
    BeerRepository beerRepository;
    
    @Mock
    BeerMapper beerMapper;

    @InjectMocks
    BeerServiceImpl beerService;

    Beer testBeer;
    BeerDto testBeerDto;
    BeerCreateRequest testBeerCreateRequest;
    BeerUpdateRequest testBeerUpdateRequest;
    LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        
        // Set up test Beer entity
        testBeer = Beer.builder()
                .id(1)
                .version(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .createdDate(now)
                .updateDate(now)
                .build();
                
        // Set up test DTOs
        testBeerDto = new BeerDto(
                1,
                1,
                "Test Beer",
                "IPA",
                "123456",
                100,
                new BigDecimal("12.99"),
                now,
                now
        );
        
        testBeerCreateRequest = new BeerCreateRequest(
                "New Beer",
                "Lager",
                "654321",
                50,
                new BigDecimal("9.99")
        );
        
        testBeerUpdateRequest = new BeerUpdateRequest(
                "Updated Beer",
                "Stout",
                "111111",
                75,
                new BigDecimal("14.99")
        );
    }

    @Test
    void testSaveBeer() {
        // Given
        Beer newBeer = Beer.builder()
                .beerName("New Beer")
                .beerStyle("Lager")
                .upc("654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build();
        
        given(beerMapper.beerCreateRequestToBeer(any(BeerCreateRequest.class))).willReturn(newBeer);
        given(beerRepository.save(any(Beer.class))).willReturn(testBeer);
        given(beerMapper.beerToBeerDto(any(Beer.class))).willReturn(testBeerDto);
        
        // When
        BeerDto savedBeerDto = beerService.saveBeer(testBeerCreateRequest);
        log.info("Saved beer DTO: {}", savedBeerDto);
        
        // Then
        assertThat(savedBeerDto).isNotNull();
        assertThat(savedBeerDto.id()).isEqualTo(1);
        assertThat(savedBeerDto.beerName()).isEqualTo("Test Beer");
        verify(beerMapper).beerCreateRequestToBeer(any(BeerCreateRequest.class));
        verify(beerRepository).save(any(Beer.class));
        verify(beerMapper).beerToBeerDto(any(Beer.class));
    }
    
    @Test
    void testGetBeerByIdFound() {
        // Given
        given(beerRepository.findById(anyInt())).willReturn(Optional.of(testBeer));
        given(beerMapper.beerToBeerDto(any(Beer.class))).willReturn(testBeerDto);
        
        // When
        Optional<BeerDto> beerDtoOptional = beerService.getBeerById(1);
        log.info("Found beer DTO: {}", beerDtoOptional);
        
        // Then
        assertTrue(beerDtoOptional.isPresent());
        BeerDto foundBeerDto = beerDtoOptional.get();
        assertThat(foundBeerDto.id()).isEqualTo(1);
        assertThat(foundBeerDto.beerName()).isEqualTo("Test Beer");
        verify(beerRepository).findById(anyInt());
        verify(beerMapper).beerToBeerDto(any(Beer.class));
    }
    
    @Test
    void testGetBeerByIdNotFound() {
        // Given
        given(beerRepository.findById(anyInt())).willReturn(Optional.empty());
        
        // When
        Optional<BeerDto> beerDtoOptional = beerService.getBeerById(1);
        log.info("Beer DTO not found: {}", beerDtoOptional);
        
        // Then
        assertFalse(beerDtoOptional.isPresent());
        verify(beerRepository).findById(anyInt());
        verify(beerMapper, never()).beerToBeerDto(any(Beer.class));
    }
    
    @Test
    void testUpdateBeerSuccess() {
        // Given
        Beer existingBeer = Beer.builder()
                .id(1)
                .version(1)
                .beerName("Original Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .createdDate(now)
                .updateDate(now)
                .build();
        
        Beer updatedBeer = Beer.builder()
                .id(1)
                .version(1)
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("111111")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(75)
                .createdDate(now)
                .updateDate(now)
                .build();
        
        BeerDto updatedBeerDto = new BeerDto(
                1,
                1,
                "Updated Beer",
                "Stout",
                "111111",
                75,
                new BigDecimal("14.99"),
                now,
                now
        );
        
        Beer mappedBeer = Beer.builder()
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("111111")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(75)
                .build();
        
        given(beerRepository.findById(anyInt())).willReturn(Optional.of(existingBeer));
        given(beerMapper.beerUpdateRequestToBeer(any(BeerUpdateRequest.class))).willReturn(mappedBeer);
        given(beerRepository.save(any(Beer.class))).willReturn(updatedBeer);
        given(beerMapper.beerToBeerDto(updatedBeer)).willReturn(updatedBeerDto);
        
        // When
        BeerDto result = beerService.updateBeer(1, testBeerUpdateRequest);
        log.info("Updated beer DTO: {}", result);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1);
        assertThat(result.beerName()).isEqualTo("Updated Beer");
        assertThat(result.beerStyle()).isEqualTo("Stout");
        assertThat(result.upc()).isEqualTo("111111");
        assertThat(result.price()).isEqualTo(new BigDecimal("14.99"));
        assertThat(result.quantityOnHand()).isEqualTo(75);
        
        verify(beerRepository).findById(anyInt());
        verify(beerMapper).beerUpdateRequestToBeer(any(BeerUpdateRequest.class));
        verify(beerRepository).save(any(Beer.class));
        verify(beerMapper).beerToBeerDto(any(Beer.class));
    }
    
    @Test
    void testUpdateBeerNotFound() {
        // Given
        given(beerRepository.findById(anyInt())).willReturn(Optional.empty());
        
        // When
        BeerDto result = beerService.updateBeer(1, testBeerUpdateRequest);
        log.info("Update result for non-existent beer: {}", result);
        
        // Then
        assertThat(result).isNull();
        verify(beerRepository).findById(anyInt());
        verify(beerRepository, never()).save(any(Beer.class));
        verify(beerMapper, never()).beerToBeerDto(any(Beer.class));
    }
    
    @Test
    void testDeleteBeerSuccess() {
        // Given
        Integer beerId = 1;
        given(beerRepository.existsById(anyInt())).willReturn(true);
        
        // When
        boolean result = beerService.deleteBeer(beerId);
        log.info("Delete result: {}", result);
        
        // Then
        assertTrue(result);
        verify(beerRepository).existsById(beerId);
        verify(beerRepository).deleteById(beerId);
    }
    
    @Test
    void testDeleteBeerNotFound() {
        // Given
        Integer beerId = 1;
        given(beerRepository.existsById(anyInt())).willReturn(false);
        
        // When
        boolean result = beerService.deleteBeer(beerId);
        log.info("Delete result for non-existent beer: {}", result);
        
        // Then
        assertFalse(result);
        verify(beerRepository).existsById(beerId);
        verify(beerRepository, never()).deleteById(anyInt());
    }
}