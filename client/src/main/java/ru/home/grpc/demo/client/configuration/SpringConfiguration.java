package ru.home.grpc.demo.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.home.grpc.demo.client.service.HelloWorldClient;

@Configuration
public class SpringConfiguration {

    @Bean
    @Scope(value = "prototype")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public HelloWorldClient getClient(String host, int port) {
        //return new HelloWorldClient("localhost", 50051);
        return new HelloWorldClient(host, port);
    }
}

