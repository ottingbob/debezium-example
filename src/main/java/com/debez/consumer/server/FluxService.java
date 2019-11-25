package com.debez.consumer.server;

import java.time.Duration;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class FluxService {

  public Flux<Long> streamWeather() {
    Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
    Flux<Long> events = Flux.fromStream(Stream.generate(
      () -> System.currentTimeMillis()
    ));

    // Flux<WeatherEvent> events = 
    //                           Flux
    //                             .fromStream(Stream.generate(
    //                             ()->new WeatherEvent(
    //                              new Weather(getTemprature(),
    //                                        getHumidity(),
    //                                        getWindSpeed()),
    //                                        LocalDateTime
    //                                        .now())));
   return Flux.zip(events, interval, (key, value) -> key);
}

}