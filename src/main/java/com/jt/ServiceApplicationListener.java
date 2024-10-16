package com.jt;

import com.jt.db.CustomMigration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ServiceApplicationListener implements org.springframework.context.ApplicationListener<ContextRefreshedEvent>{

    private CustomMigration customMigration;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        customMigration.runMigrations();
        customMigration.insertData();
    }

}
