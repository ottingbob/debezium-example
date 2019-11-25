package com.debez.consumer.server;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@RestControllerEndpoint(id = "rb-metrics")
public class RebalanceHandler {

  public static final AtomicInteger COUNTER = new AtomicInteger(0); 

  // @ReadOperation
  // @GetMapping("/test")
  @GetMapping
  public @ResponseBody ResponseEntity endpoint() {
    return new ResponseEntity<>("Called rebalance metrics endpoint: " + COUNTER.get(), HttpStatus.OK);
  }
}

 /**
  * 
  @Component
@RestControllerEndpoint(id = "rest-end-point")
public class RestCustomEndPoint {

    @GetMapping("/custom")
    public @ResponseBody ResponseEntity customEndPoint(){
        return  new ResponseEntity<>("REST end point", HttpStatus.OK);
    }
}
  */