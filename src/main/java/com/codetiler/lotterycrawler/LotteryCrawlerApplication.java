package com.codetiler.lotterycrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LotteryCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryCrawlerApplication.class, args);
    }

}
