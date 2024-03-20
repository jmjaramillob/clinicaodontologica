package com.backend.clinicaodontologica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClinicaodontologicaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClinicaodontologicaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ClinicaodontologicaApplication.class, args);
        LOGGER.info("ClinicaOdontologica is now running...");
    }


}
