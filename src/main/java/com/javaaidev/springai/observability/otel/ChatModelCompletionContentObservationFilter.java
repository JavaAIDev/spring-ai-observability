package com.javaaidev.springai.observability.otel;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;
import java.util.List;
import java.util.Objects;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.observation.ChatModelObservationContext;
import org.springframework.ai.content.Content;
import org.springframework.ai.observation.ObservabilityHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class ChatModelCompletionContentObservationFilter implements ObservationFilter {

  @Override
  public Observation.Context map(final Observation.Context context) {
    if (!(context instanceof ChatModelObservationContext chatModelObservationContext)) {
      return context;
    }

    final var prompts = this.processPrompts(chatModelObservationContext);
    final var completions = this.processCompletion(chatModelObservationContext);

    chatModelObservationContext.addHighCardinalityKeyValue(
        KeyValue.of("gen_ai.prompt", ObservabilityHelper.concatenateStrings(prompts)));

    chatModelObservationContext.addHighCardinalityKeyValue(
        KeyValue.of("gen_ai.completion", ObservabilityHelper.concatenateStrings(completions)));

    return chatModelObservationContext;
  }

  private List<String> processPrompts(
      final ChatModelObservationContext chatModelObservationContext) {
    final var instructions = chatModelObservationContext.getRequest().getInstructions();
    return CollectionUtils.isEmpty(instructions)
        ? List.of()
        : instructions.stream().map(Content::getText).toList();
  }

  private List<String> processCompletion(final ChatModelObservationContext context) {
    if (context.getResponse() == null) {
      return List.of();
    }

    final var results = context.getResponse().getResults();
    if (CollectionUtils.isEmpty(results)) {
      return List.of();
    }

    return results.stream()
        .map(Generation::getOutput)
        .filter(Objects::nonNull)
        .map(AbstractMessage::getText)
        .filter(StringUtils::hasText)
        .toList();
  }
}
