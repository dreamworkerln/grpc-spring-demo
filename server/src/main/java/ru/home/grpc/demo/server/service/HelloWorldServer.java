/*
 * Copyright 2015 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.home.grpc.demo.server.service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import ru.home.grpc.demo.helloworld.HelloReply;
import ru.home.grpc.demo.helloworld.HelloRequest;
import ru.home.grpc.demo.helloworld.GreeterGrpc;

import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */

@Service
public class HelloWorldServer {
    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Server server;

    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;

        server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();

        log.info("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the log may have been reset by its JVM shutdown hook.
            
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            log.error("Shutting down gRPC server since JVM is shutting down");

            HelloWorldServer.this.stop();

            System.err.println("*** server shut down");
        }));
    }

    private void stop() {

        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {

            server.awaitTermination();
        }
    }

    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

        @Override
        public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {

            log.info("Received hello from: " + req.getName());

            String message = "Hello " + req.getName();

            HelloReply reply = HelloReply.newBuilder().setMessage(message).build();

            log.info("Sending message to client: " + message);
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }



    @PostConstruct
    @Async
    public void startup() throws IOException {
        this.start();
    }


}



//    /**
//     * Main launches the server from the command line.
//     */
//    public static void main(String[] args) throws IOException, InterruptedException {
//        final HelloWorldServer server = new HelloWorldServer();
//        server.start();
//        server.blockUntilShutdown();
//    }
