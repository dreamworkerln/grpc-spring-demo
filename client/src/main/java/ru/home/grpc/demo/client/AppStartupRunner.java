package ru.home.grpc.demo.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.home.grpc.demo.client.service.HelloWorldClient;

import java.lang.invoke.MethodHandles;


@Component
public class AppStartupRunner implements ApplicationRunner {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ApplicationContext context;

    public AppStartupRunner(ApplicationContext context) {
        this.context = context;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("Connecting to server ...");
        HelloWorldClient client = context.getBean(HelloWorldClient.class, "localhost", 50051);
        log.info("Sending greeting ...");

        String message = client.greet("MyUserName");
        log.info("Response: " + message);

        log.info("Disconnecting ...");
        client.shutdown();
    }
}