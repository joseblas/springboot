package com.jt.adapters;

import com.jt.application.PriceService;
import com.jt.model.GetPriceResponseContent;
import com.jt.openapi.ApiPriceController;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class PriceController implements ApiPriceController {

    private PriceService priceService;

    @Override
    public Mono<ResponseEntity<GetPriceResponseContent>> getPrice(
            @NotNull @Parameter(name = "brandId", description = "", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "brandId", required = true) Integer brandId,
            @NotNull @Parameter(name = "productId", description = "", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "productId", required = true) Integer productId,
            @NotNull @Parameter(name = "applicationDate", description = "", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "applicationDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime applicationDate,
            @Parameter(hidden = true) final ServerWebExchange exchange
    ) {
        return priceService.getApplicablePrice(brandId, productId, applicationDate)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
