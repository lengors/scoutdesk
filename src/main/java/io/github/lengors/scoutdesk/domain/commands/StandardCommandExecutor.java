package io.github.lengors.scoutdesk.domain.commands;


import io.github.lengors.scoutdesk.domain.common.Result;
import io.github.lengors.scoutdesk.domain.resolvers.ResolutionService;
import io.micrometer.tracing.SpanName;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Qualifier("standard")
class StandardCommandExecutor implements CommandExecutor {
  private static final Logger LOG = LoggerFactory.getLogger(StandardCommandExecutor.class);

  private final ResolutionService<@NotNull Command<?, ?>, @NotNull CommandHandler<?, ?, ?>> resolutionService;

  StandardCommandExecutor(
    @Lazy final ResolutionService<@NotNull Command<?, ?>, @NotNull CommandHandler<?, ?, ?>> resolutionService
  ) {
    this.resolutionService = resolutionService;
  }

  @Override
  @SuppressWarnings("nullness")
  public <C extends Command<I, O>, I, O> Result<O, ? extends RuntimeException> execute(
    final CommandRequest<C, I, O> commandRequest
  ) {
    final var command = commandRequest.command();
    final var input = commandRequest.input();
    final var metadata = commandRequest.metadata();

    final Result<O, ? extends RuntimeException> result;
    MDC.put("metadata", metadata.toString());
    try {
      LOG.trace("Resolving handler for {command={}}", command);
      final var commandHandler = resolve(command);
      LOG.debug("Resolved handler for {command={}}: {}", command, commandHandler);

      result = execute(commandHandler, command, input);
      log(command, input, result);
    } finally {
      MDC.remove("metadata");
    }

    return result;
  }

  @SpanName("executeCommand")
  private <C extends Command<I, O>, I, O> Result<O, ? extends RuntimeException> execute(
    final CommandHandler<C, I, O> commandHandler,
    final C command,
    final I input
  ) {
    return Result.wrap(() -> commandHandler.handle(command, input));
  }

  @SuppressWarnings("unchecked")
  private <C extends Command<I, O>, I, O> CommandHandler<C, I, O> resolve(final C command) {
    return (CommandHandler<C, I, O>) resolutionService.resolve(command);
  }

  @SuppressWarnings("nullness")
  private static <C extends Command<I, O>, I, O> void log(
    final C command,
    final I input,
    final Result<O, ?> result
  ) {
    result.ifSuccessfulOrElse(
      output -> {
        if (LOG.isTraceEnabled()) {
          LOG.trace("Executed {command={}, input={}, output={}}", command, input, output);
        } else if (LOG.isDebugEnabled()) {
          LOG.debug("Executed {command={}, input={}}", command, input);
        } else {
          LOG.info("Executed {command={}}", command);
        }
      },
      (Throwable throwable) -> {
        if (LOG.isDebugEnabled()) {
          LOG.error("Failed to execute command {command={}, input={}}", command, input, throwable);
        } else {
          LOG.error("Failed to execute command {command={}}", command, throwable);
        }
      }
    );
  }
}
