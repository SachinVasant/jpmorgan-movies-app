package com.svasant.movies.writer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.svasant.movies"})
public class WriterApplication {
    public static void main(String[] args) {
        SpringApplication.run(WriterApplication.class, args);
    }
}
