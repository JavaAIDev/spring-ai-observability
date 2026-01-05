package com.javaaidev.springai.observability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SpringAiObservabilityApplication {

  public static void main(final String[] args) {
    SpringApplication.run(SpringAiObservabilityApplication.class, args);
  }
}
