package com.inditex.inditex.infrastructure;

import com.inditex.inditex.domain.Price;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface PriceRepository extends ReactiveCrudRepository<Price, Long> {
    @Query("SELECT * FROM prices WHERE brand_id = :brandId AND product_id = :productId AND :applicationDate BETWEEN start_date AND end_date ORDER BY priority DESC LIMIT 1")
    Mono<Price> findApplicablePrice(@Param("brandId") int brandId, @Param("productId") int productId, @Param("applicationDate") LocalDateTime applicationDate);
}
