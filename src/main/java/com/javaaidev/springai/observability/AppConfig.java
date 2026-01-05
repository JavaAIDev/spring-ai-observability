package com.javaaidev.springai.observability;

import com.javaaidev.springai.observability.otel.ChatModelCompletionContentObservationFilter;
import com.javaaidev.springai.observability.otel.ToolCallingContentObservationFilter;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientCustomizer;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Configuration
public class AppConfig {

  @Bean
  public ApplicationRunner startupRunner(
      final McpAsyncClientInitializer mcpAsyncClientInitializer) {
    return args -> mcpAsyncClientInitializer.runAsyncTask();
  }

  @Bean
  public ChatClientCustomizer chatClientCustomizer(
      final AsyncMcpToolCallbackProvider toolCallbackProvider) {
    return builder -> builder.defaultToolCallbacks(toolCallbackProvider);
  }

  @Bean
  public ChatModelCompletionContentObservationFilter chatModelCompletionContentObservationFilter() {
    return new ChatModelCompletionContentObservationFilter();
  }

  @Bean
  public ToolCallingContentObservationFilter toolCallingContentObservationFilter() {
    return new ToolCallingContentObservationFilter();
  }

  @Service
  public static class McpAsyncClientInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(McpAsyncClientInitializer.class);
    private final AsyncMcpToolCallbackProvider mcpToolCallbackProvider;

    public McpAsyncClientInitializer(final AsyncMcpToolCallbackProvider mcpToolCallbackProvider) {
      this.mcpToolCallbackProvider = mcpToolCallbackProvider;
    }

    @Async
    public void runAsyncTask() {
      final var toolCallbacks = this.mcpToolCallbackProvider.getToolCallbacks();
      LOGGER.info(
          "Found tools: {}",
          Arrays.stream(toolCallbacks).map(t -> t.getToolDefinition().name()).toList());
    }
  }
}
