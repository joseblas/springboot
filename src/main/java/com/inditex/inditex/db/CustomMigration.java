package com.inditex.inditex.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class CustomMigration {

    private final DatabaseClient databaseClient;
    private final Environment environment;

    public void insertData() {
        if(environment.acceptsProfiles(Profiles.of("local","test","loadtesting"))) {
            log.info("Inserting data for local, test and loadtesting profiles");
            databaseClient.sql("""
                TRUNCATE TABLE PRICES;
                INSERT INTO PRICES (brand_id, start_date, end_date, price_list, product_id, priority, price, curr)
                VALUES
                    (1, '2020-06-14 00:00:00', '2020-12-31 23:59:59', 1, 35455, 0, 35.50, 'EUR'),
                    (1, '2020-06-14 15:00:00', '2020-06-14 18:30:00', 2, 35455, 1, 25.45, 'EUR'),
                    (1, '2020-06-15 00:00:00', '2020-06-15 11:00:00', 3, 35455, 1, 30.50, 'EUR'),
                    (1, '2020-06-15 16:00:00', '2020-12-31 23:59:59', 4, 35455, 1, 38.95, 'EUR');
                """).then().subscribe();
        }
    }

    public void runMigrations() {
        log.info("Running migrations");
        databaseClient.sql("""
            CREATE TABLE IF NOT EXISTS PRICES (
                        id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        brand_id INTEGER NOT NULL,
                        start_date TIMESTAMP NOT NULL,
                        end_date TIMESTAMP NOT NULL,
                        price_list INTEGER NOT NULL,
                        product_id BIGINT NOT NULL,
                        priority INTEGER NOT NULL,
                        price DECIMAL(10, 2) NOT NULL,
                        curr VARCHAR(3) NOT NULL
            );
            DO $$
            BEGIN
                IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'idx_prices_brand_product') THEN
                    CREATE INDEX idx_prices_brand_product ON prices (brand_id, product_id);
                END IF;
                IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'idx_prices_start_date') THEN
                    CREATE INDEX idx_prices_start_date ON prices (start_date);
                END IF;
                IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'idx_prices_end_date') THEN
                    CREATE INDEX idx_prices_end_date ON prices (end_date);
                END IF;
                IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'idx_prices_priority') THEN
                    CREATE INDEX idx_prices_priority ON prices (priority);
                END IF;
            END
            $$;
            """.strip())
                .then().subscribe();
    }
}
