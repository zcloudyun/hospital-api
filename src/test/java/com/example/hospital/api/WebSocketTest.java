package com.example.hospital.api;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketTest {
    private static final AtomicInteger count = new AtomicInteger(0);

    @Test
    public void test() {
        URI uri = URI.create("ws://127.0.0.1:8092/chat?token=123");  //注意协议号为ws

    }

}