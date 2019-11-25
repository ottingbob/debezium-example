package com.debez.consumer.server;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class HealthHandler implements ReactiveHealthIndicator {

  @Override
  public Mono<Health> health() {
    return checkHealth().onErrorResume(
      ex -> Mono.just(new Health.Builder().down(ex).build()));
  }

  private Mono<Health> checkHealth() {
    // Could use WebClient to check health reactively
    if (RebalanceHandler.COUNTER.get() > 10) {
      return Mono.just(new Health.Builder(Status.DOWN).build());
    }

    return Mono.just(new Health.Builder().up().build());
  }
  
}
