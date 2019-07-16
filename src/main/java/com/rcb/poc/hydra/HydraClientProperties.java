package com.rcb.poc.hydra;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class HydraClientProperties {

    @Value("${hydra.basePath}")
    private String basePath;
}
