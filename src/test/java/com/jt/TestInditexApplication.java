package com.jt;

import org.springframework.boot.SpringApplication;

public class TestInditexApplication {

    public static void main(String[] args) {
        SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
