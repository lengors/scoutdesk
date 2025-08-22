package io.github.lengors.scoutdesk.domain.commands;

import io.github.lengors.scoutdesk.domain.core.Result;
import io.github.lengors.scoutdesk.domain.resolvers.ResolutionService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Map;

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

    LOG.trace("{} | Resolving handler for {command={}}", metadata, command);
    final var commandFunction = resolve(command);
    LOG.debug("{} | Resolved handler for {command={}}: {}", metadata, command, commandFunction);

    final var stopWatch = new StopWatch();
    stopWatch.start();
    final var result = Result.wrap(() -> commandFunction.handle(command, input));
    stopWatch.stop();
    log(command, input, metadata, result, stopWatch.getTotalTimeMillis());

    return result;
  }

  @SuppressWarnings("unchecked")
  private <C extends Command<I, O>, I, O> CommandHandler<C, I, O> resolve(final C command) {
    return (CommandHandler<C, I, O>) resolutionService.resolve(command);
  }

  @SuppressWarnings("nullness")
  private static <C extends Command<I, O>, I, O> void log(
    final C command,
    final I input,
    final Map<?, ?> metadata,
    final Result<O, ?> result,
    final long elapsedTimeMillis
  ) {
    result.ifSuccessfulOrElse(
      output -> {
        if (LOG.isTraceEnabled()) {
          LOG.trace(
            "{} | Executed {command={}, input={}, output={}} in {} ms",
            metadata,
            command,
            input,
            output,
            elapsedTimeMillis);
        } else if (LOG.isDebugEnabled()) {
          LOG.debug("{} | Executed {command={}, input={}} in {} ms", metadata, command, input, elapsedTimeMillis);
        } else {
          LOG.info("{} | Executed {command={}} in {} ms", metadata, command, elapsedTimeMillis);
        }
      },
      (Throwable throwable) -> {
        if (LOG.isDebugEnabled()) {
          LOG.error(
            "{} | Failed to execute command {command={}, input={}} in {} ms",
            metadata,
            command,
            input,
            elapsedTimeMillis,
            throwable);
        } else {
          LOG.error(
            "{} | Failed to execute command {command={}} in {} ms",
            metadata,
            command,
            elapsedTimeMillis,
            throwable);
        }
      }
    );
  }
}
