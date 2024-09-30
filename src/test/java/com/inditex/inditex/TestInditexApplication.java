package com.inditex.inditex;

import org.springframework.boot.SpringApplication;

public class TestInditexApplication {

    public static void main(String[] args) {
        SpringApplication.from(InditexApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
