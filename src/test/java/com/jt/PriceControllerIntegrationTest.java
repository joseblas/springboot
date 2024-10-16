package com.jt;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.stream.Stream;


@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Testcontainers
public class PriceControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private WebTestClient webTestClient;


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> String.format("r2dbc:postgresql://%s:%d/%s", postgresContainer.getHost(), postgresContainer.getFirstMappedPort(), postgresContainer.getDatabaseName()));
        registry.add("spring.r2dbc.username", postgresContainer::getUsername);
        registry.add("spring.r2dbc.password", postgresContainer::getPassword);
    }

    public static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(1, 35455, "2020-06-14T10:00:00", 35.50, 1, "2020-06-14T00:00:00", "2020-12-31T23:59:59"),
                Arguments.of(1, 35455, "2020-06-14T16:00:00", 25.45, 2, "2020-06-14T15:00:00", "2020-06-14T18:30:00"),
                Arguments.of(1, 35455, "2020-06-14T21:00:00", 35.50, 1, "2020-06-14T00:00:00", "2020-12-31T23:59:59"),
                Arguments.of(1, 35455, "2020-06-15T10:00:00", 30.50, 3, "2020-06-15T00:00:00", "2020-06-15T11:00:00"),
                Arguments.of(1, 35455, "2020-06-16T21:00:00", 38.95, 4, "2020-06-15T16:00:00", "2020-12-31T23:59:59")
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void testGetPrice(int brandId, int productId, String applicationDate, double expectedPrice, int priceList, String startDate, String endDate) {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/prices")
                        .queryParam("brandId", brandId)
                        .queryParam("productId", productId)
                        .queryParam("applicationDate", applicationDate)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(productId)
                .jsonPath("$.priceList").isEqualTo(priceList)
                .jsonPath("$.startDate").isEqualTo(startDate)
                .jsonPath("$.endDate").isEqualTo(endDate)
                .jsonPath("$.price").isEqualTo(expectedPrice);
    }


    public static Stream<Arguments> testNotFound() {
        return Stream.of(
                Arguments.of(10, 35455, "2020-06-14T10:00:00"),
                Arguments.of(1, 1, "2020-06-14T16:00:00"),
                Arguments.of(1, 35455, "2025-06-14T21:00:00")
        );
    }


    @ParameterizedTest
    @MethodSource("testNotFound")
    public void testNotFound(int brandId, int productId, String applicationDate) {
        System.out.println("brandId: " + brandId + ", productId: " + productId + ", applicationDate: " + applicationDate);
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/prices")
                        .queryParam("brandId", brandId)
                        .queryParam("productId", productId)
                        .queryParam("applicationDate", applicationDate)
                        .build())
                .exchange()
                .expectStatus().isNotFound();
    }

    public static Stream<Arguments> testWrongType() {
        return Stream.of(
                Arguments.of(10, 35455, "2020-06-14T"),
                Arguments.of("ZARA", 1, "2020-06-14T16:00:00"),
                Arguments.of(1, "ZAPATOS", "2025-06-14T21:00:00")
        );
    }


    @ParameterizedTest
    @MethodSource("testWrongType")
    public void testWrongType(Object brandId, Object productId, String applicationDate) {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/prices")
                        .queryParam("brandId", brandId)
                        .queryParam("productId", productId)
                        .queryParam("applicationDate", applicationDate)
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

}
