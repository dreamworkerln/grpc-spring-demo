package ru.home.grpc.demo.client.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.home.grpcdemo.helloworld.GreeterGrpc;
import ru.home.grpcdemo.helloworld.HelloReply;
import ru.home.grpcdemo.helloworld.HelloRequest;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;


/**
 * A simple client that requests a greeting from the {@link HelloWorldServer}.
 */
public class HelloWorldClient {
    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ManagedChannel channel;
    private GreeterGrpc.GreeterBlockingStub blockingStub;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public HelloWorldClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
            // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
            // needing certificates.
            .usePlaintext()
            .build());
    }

    /** Construct client for accessing HelloWorld server using the existing channel. */
    private HelloWorldClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }



    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Say hello to server. */
    public String greet(String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            log.warn("RPC failed {}", e.getStatus());

            return null;
        }
        return response.getMessage();
    }

}