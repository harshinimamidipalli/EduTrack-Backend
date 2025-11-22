package com.edutrack.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import java.time.Duration;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    @GetMapping("/stream")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return sink.asFlux()
                .map(data -> ServerSentEvent.builder(data).build())
                .mergeWith(Flux.interval(Duration.ofSeconds(30)).map(i -> ServerSentEvent.builder("ping").build()));
    }

    public void sendNotification(String message) {
        sink.tryEmitNext(message);
    }

    // This endpoint is just for triggering notifications manually from code
    @PostMapping("/trigger")
    public void trigger(@RequestBody String message) {
        sendNotification(message);
    }
}
