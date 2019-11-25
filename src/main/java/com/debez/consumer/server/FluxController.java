package com.debez.consumer.server;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class FluxController {

  private final FluxService service;

  @GetMapping("/flux")
  public Flux<Long> getFlux() {
    return service.streamWeather();
  }

  @GetMapping(value = "/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<Long> priceStream(){
    return Flux.interval(Duration.ofMillis(500))
            .map(l -> System.currentTimeMillis()) // new Price(System.currentTimeMillis(), ThreadLocalRandom.current().nextInt(100, 125)))
            .log();
}

}