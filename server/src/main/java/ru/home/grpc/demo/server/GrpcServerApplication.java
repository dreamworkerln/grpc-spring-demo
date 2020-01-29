package ru.home.grpc.demo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
public class GrpcServerApplication {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {

        SpringApplication.run(GrpcServerApplication.class, args);
    }

    @Scheduled(fixedDelay = 3600) // every hour
    public void doNothing() {
        // Forces Spring Scheduling managing thread to start
        // and prevent Spring Application to shut down
    }

}
