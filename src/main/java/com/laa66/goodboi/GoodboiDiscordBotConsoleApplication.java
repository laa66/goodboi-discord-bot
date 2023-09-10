package com.laa66.goodboi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootApplication
public class GoodboiDiscordBotConsoleApplication {

    @Bean
    CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> run(context);
    }

    public static void main(String[] args) {
        log.info("Starting Goodboi console app");
    //    SpringApplication.run(GoodboiDiscordBotConsoleApplication.class, args);
        log.info("Exiting Goodboi console app");
    }

    public void run(ApplicationContext context) {
        log.info("Running...");
        CountDownLatch latch = new CountDownLatch(1);
        latch.countDown();
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
