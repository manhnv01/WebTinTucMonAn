package com.foodei.project.config;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class WebConfig {
    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public Slugify slugify() {
        return new Slugify();
    }
    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }



}
