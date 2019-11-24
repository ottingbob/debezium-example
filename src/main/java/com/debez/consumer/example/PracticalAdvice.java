package com.debez.consumer.example;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PracticalAdvice {

  private final String message;
  private final int identifier;

  public PracticalAdvice(
      @JsonProperty("message") final String message, 
      @JsonProperty("identifier") final int identifier) {
    this.message = message;
    this.identifier = identifier;
  }

}