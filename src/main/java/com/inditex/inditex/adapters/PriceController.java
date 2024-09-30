package com.inditex.inditex.adapters;

import com.inditex.inditex.domain.Price;
import com.inditex.inditex.application.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/prices")
@AllArgsConstructor
public class PriceController {

    private PriceService priceService;

    @GetMapping
    public Mono<ResponseEntity<Price>> getPrice(@RequestParam int brandId, @RequestParam int productId, @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime applicationDate) {
        return priceService.getApplicablePrice(brandId, productId, applicationDate)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
