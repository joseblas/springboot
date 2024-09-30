package com.inditex.inditex.application;

import com.inditex.inditex.domain.Price;
import com.inditex.inditex.infrastructure.PriceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class PriceService {
    private PriceRepository priceRepository;

    public Mono<Price> getApplicablePrice(int brandId, int productId, LocalDateTime applicationDate) {
        log.debug("Getting applicable price for brandId: {}, productId: {}, applicationDate: {}", brandId, productId, applicationDate);
        return priceRepository.findApplicablePrice(brandId, productId, applicationDate);
    }
}
