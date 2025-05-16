package io.github.lengors.scoutdesk.domain.commands.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for executing commands in the application.
 *
 * Provides methods to resolve and execute command handlers for various command
 * types.
 *
 * @author lengors
 */
@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CommandService {
  private final List<CommandHandlerResolver> commandHandlerResolvers;

  /**
   * Executes a command using the appropriate command handler.
   *
   * @param <C>     the command type
   * @param <I>     the input type
   * @param <O>     the output type
   * @param command the command to execute
   * @param input   the input for the command
   * @return the output of the command
   */
  public <C extends Command<I, O>, I, O> O executeCommand(final C command, final I input) {
    log.trace("Resolving handler for {command={}}", command);
    final var commandHandler = resolveCommandHandler(command);
    log.debug("Resolved handler for {command={}}: {}", command, commandHandler);

    final var stopWatch = new StopWatch();
    stopWatch.start();
    final var output = commandHandler.handle(command, input);
    stopWatch.stop();

    logResult(command, input, output, stopWatch);

    return output;
  }

  private <C extends Command<I, O>, I, O> CommandHandler<C, I, O> resolveCommandHandler(final C command) {
    for (final var commandHandlerResolver : this.commandHandlerResolvers) {
      final var commandHandler = commandHandlerResolver.resolve(command);
      if (commandHandler != null) {
        return commandHandler;
      }
    }
    throw new IllegalArgumentException("No command handler found for command: " + command
        .getClass()
        .getName());
  }

  @SuppressWarnings("nullness")
  private static <C extends Command<I, O>, I, O> void logResult(final C command, final I input, final O output,
      final StopWatch stopWatch) {
    if (log.isTraceEnabled()) {
      log.trace(
          "Executed {command={}, input={}, output={}} in {} ms",
          command,
          input,
          output,
          stopWatch.getTotalTimeMillis());
    } else if (log.isDebugEnabled()) {
      log.debug("Executed {command={}, input={}} in {} ms", command, input, stopWatch.getTotalTimeMillis());
    } else {
      log.info("Executed {command={}} in {} ms", command, stopWatch.getTotalTimeMillis());
    }
  }
}
