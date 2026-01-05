package com.javaaidev.springai.observability.otel;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;
import org.springframework.ai.tool.observation.ToolCallingObservationContext;

public class ToolCallingContentObservationFilter implements ObservationFilter {

  @Override
  public Observation.Context map(final Observation.Context context) {
    if (!(context instanceof ToolCallingObservationContext toolCallingObservationContext)) {
      return context;
    }

    final var toolDefinition = toolCallingObservationContext.getToolDefinition();
    toolCallingObservationContext.addLowCardinalityKeyValue(
        KeyValue.of("gen_ai.tool.name", toolDefinition.name()));
    toolCallingObservationContext.addHighCardinalityKeyValue(
        KeyValue.of("gen_ai.tool.description", toolDefinition.description()));

    toolCallingObservationContext
        .addHighCardinalityKeyValue(
            KeyValue.of("gen_ai.tool.call.arguments",
                toolCallingObservationContext.getToolCallArguments()));

    final String toolCallResult = toolCallingObservationContext.getToolCallResult();
    if (toolCallResult != null) {
      toolCallingObservationContext
          .addHighCardinalityKeyValue(
              KeyValue.of("gen_ai.tool.call.result", toolCallResult));
    }

    return toolCallingObservationContext;
  }

}
