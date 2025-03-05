package com.parking.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.parking.Repository.CarRepositoryImpl;

@Configuration
public class ParkingConfig {
    @Bean
    public CarRepositoryImpl carRepository() {
        return new CarRepositoryImpl();
    }
}