package com.inditex.inditex.adapters;

import com.inditex.inditex.domain.Price;
import com.inditex.inditex.application.PriceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class PriceControllerTest {

    PriceService priceService = Mockito.mock(PriceService.class);
    LocalDateTime now = LocalDateTime.now();

    Price price = Price.builder()
            .brandId(1)
            .productId(2)
            .priceList(3)
            .endDate(now)
            .startDate(now)
            .curr("GBP")
            .price(14.99)
            .build();

    @Test
    void getPrice_with_valid_data() {
        PriceController priceController = new PriceController(priceService);
        when(priceService.getApplicablePrice(anyInt(), anyInt(), any())).thenReturn(Mono.just(price));

        ResponseEntity<Price> response = priceController.getPrice(1, 1, LocalDateTime.now()).block();
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(price);
    }

    @Test
    void getPrice_with_no_data() {
        PriceController priceController = new PriceController(priceService);
        when(priceService.getApplicablePrice(anyInt(), anyInt(), any())).thenReturn(Mono.empty());

        ResponseEntity<Price> response = priceController.getPrice(1, 1, LocalDateTime.now()).block();
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}