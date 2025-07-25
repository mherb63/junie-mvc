package com.meh.juniemvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meh.juniemvc.domain.BeerCreateRequest;
import com.meh.juniemvc.domain.BeerDto;
import com.meh.juniemvc.domain.BeerUpdateRequest;
import com.meh.juniemvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    BeerDto testBeerDto;
    BeerCreateRequest validBeerCreateRequest;
    BeerUpdateRequest validBeerUpdateRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        
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
        
        validBeerCreateRequest = new BeerCreateRequest(
                "New Beer",
                "Lager",
                "654321",
                50,
                new BigDecimal("9.99")
        );
        
        validBeerUpdateRequest = new BeerUpdateRequest(
                "Updated Beer",
                "Stout",
                "111111",
                75,
                new BigDecimal("14.99")
        );
    }

    @Test
    void testCreateBeer() throws Exception {
        // Given
        given(beerService.saveBeer(any(BeerCreateRequest.class))).willReturn(testBeerDto);

        // When/Then
        mockMvc.perform(post("/api/v1/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validBeerCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Test Beer")));

        verify(beerService).saveBeer(any(BeerCreateRequest.class));
    }
    
    @Test
    void testCreateBeerValidationError() throws Exception {
        // Given
        BeerCreateRequest invalidRequest = new BeerCreateRequest(
                "", // Invalid: empty beer name
                "Lager",
                "654321",
                50,
                new BigDecimal("9.99")
        );

        // When/Then
        mockMvc.perform(post("/api/v1/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBeer() throws Exception {
        // Given
        BeerDto updatedBeerDto = new BeerDto(
                1,
                1,
                "Updated Beer",
                "Stout",
                "111111",
                75,
                new BigDecimal("14.99"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(beerService.updateBeer(anyInt(), any(BeerUpdateRequest.class))).willReturn(updatedBeerDto);

        // When/Then
        mockMvc.perform(put("/api/v1/beer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validBeerUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Updated Beer")))
                .andExpect(jsonPath("$.beerStyle", is("Stout")))
                .andExpect(jsonPath("$.upc", is("111111")))
                .andExpect(jsonPath("$.price", is(14.99)))
                .andExpect(jsonPath("$.quantityOnHand", is(75)));

        verify(beerService).updateBeer(anyInt(), any(BeerUpdateRequest.class));
    }
    
    @Test
    void testUpdateBeerValidationError() throws Exception {
        // Given
        BeerUpdateRequest invalidRequest = new BeerUpdateRequest(
                "Updated Beer",
                "", // Invalid: empty beer style
                "111111",
                75,
                new BigDecimal("14.99")
        );

        // When/Then
        mockMvc.perform(put("/api/v1/beer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBeerNotFound() throws Exception {
        // Given
        given(beerService.updateBeer(anyInt(), any(BeerUpdateRequest.class))).willReturn(null);

        // When/Then
        mockMvc.perform(put("/api/v1/beer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validBeerUpdateRequest)))
                .andExpect(status().isNotFound());

        verify(beerService).updateBeer(anyInt(), any(BeerUpdateRequest.class));
    }

    @Test
    void testDeleteBeer() throws Exception {
        // Given
        given(beerService.deleteBeer(anyInt())).willReturn(true);

        // When/Then
        mockMvc.perform(delete("/api/v1/beer/1"))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeer(anyInt());
    }

    @Test
    void testDeleteBeerNotFound() throws Exception {
        // Given
        given(beerService.deleteBeer(anyInt())).willReturn(false);

        // When/Then
        mockMvc.perform(delete("/api/v1/beer/1"))
                .andExpect(status().isNotFound());

        verify(beerService).deleteBeer(anyInt());
    }

    @Test
    void testGetBeerById() throws Exception {
        // Given
        given(beerService.getBeerById(anyInt())).willReturn(Optional.of(testBeerDto));

        // When/Then
        mockMvc.perform(get("/api/v1/beer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Test Beer")))
                .andExpect(jsonPath("$.beerStyle", is("IPA")));

        verify(beerService).getBeerById(anyInt());
    }

    @Test
    void testGetBeerByIdNotFound() throws Exception {
        // Given
        given(beerService.getBeerById(anyInt())).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/v1/beer/1"))
                .andExpect(status().isNotFound());

        verify(beerService).getBeerById(anyInt());
    }
}