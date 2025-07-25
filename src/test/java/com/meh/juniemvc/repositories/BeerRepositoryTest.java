package com.meh.juniemvc.repositories;

import com.meh.juniemvc.domain.Beer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        // When
        Beer savedBeer = beerRepository.save(beer);
        log.info("Saved beer: {}", savedBeer);

        // Then
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
        assertThat(savedBeer.getCreatedDate()).isNotNull();
        assertThat(savedBeer.getUpdateDate()).isNotNull();
    }

    @Test
    void testGetBeerById() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Beer savedBeer = beerRepository.save(beer);

        // When
        Optional<Beer> fetchedBeerOptional = beerRepository.findById(savedBeer.getId());
        log.info("Fetched beer: {}", fetchedBeerOptional);

        // Then
        assertTrue(fetchedBeerOptional.isPresent());
        Beer fetchedBeer = fetchedBeerOptional.get();
        assertThat(fetchedBeer.getBeerName()).isEqualTo("Test Beer");
        assertThat(fetchedBeer.getBeerStyle()).isEqualTo("IPA");
    }

    @Test
    void testUpdateBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Original Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Beer savedBeer = beerRepository.save(beer);

        // When
        savedBeer.setBeerName("Updated Beer");
        savedBeer.setPrice(new BigDecimal("14.99"));
        Beer updatedBeer = beerRepository.save(savedBeer);
        log.info("Updated beer: {}", updatedBeer);

        // Then
        assertThat(updatedBeer.getBeerName()).isEqualTo("Updated Beer");
        assertThat(updatedBeer.getPrice()).isEqualTo(new BigDecimal("14.99"));
        assertThat(updatedBeer.getUpdateDate()).isNotNull();
    }

    @Test
    void testDeleteBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Delete Me Beer")
                .beerStyle("Lager")
                .upc("654321")
                .price(new BigDecimal("9.99"))
                .quantityOnHand(50)
                .build();
        Beer savedBeer = beerRepository.save(beer);
        Integer beerId = savedBeer.getId();

        // When
        log.info("Deleting beer with ID: {}", beerId);
        beerRepository.deleteById(beerId);
        Optional<Beer> deletedBeerOptional = beerRepository.findById(beerId);

        // Then
        assertFalse(deletedBeerOptional.isPresent());
    }

    @Test
    void testFindAllBeers() {
        // Given
        beerRepository.deleteAll(); // Clear any existing data
        
        Beer beer1 = Beer.builder()
                .beerName("Beer One")
                .beerStyle("IPA")
                .upc("111111")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(100)
                .build();
                
        Beer beer2 = Beer.builder()
                .beerName("Beer Two")
                .beerStyle("Stout")
                .upc("222222")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(200)
                .build();
                
        beerRepository.saveAll(List.of(beer1, beer2));

        // When
        List<Beer> beers = beerRepository.findAll();
        log.info("Beers: {}", beers);

        // Then
        assertThat(beers).isNotNull();
        assertThat(beers.size()).isEqualTo(2);
    }
}