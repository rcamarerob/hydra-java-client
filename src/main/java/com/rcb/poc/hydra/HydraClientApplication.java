package com.rcb.poc.hydra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HydraClientApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HydraClientApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HydraClientApplication.class);
    }
}
