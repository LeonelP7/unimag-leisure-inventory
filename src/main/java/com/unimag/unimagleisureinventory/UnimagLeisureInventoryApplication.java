package com.unimag.unimagleisureinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UnimagLeisureInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnimagLeisureInventoryApplication.class, args);
    }

}
