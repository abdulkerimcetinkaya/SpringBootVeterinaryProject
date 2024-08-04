package com.project.vetProject.core.config.modelMapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelManagerConfig {

    // ModelMapper bean'i tanımlar
    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }
}
