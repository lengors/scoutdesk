package io.github.lengors.scoutdesk.domain.commands.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import io.github.lengors.scoutdesk.domain.commands.models.Command;

/**
 * Service for executing commands in the application.
 *
 * Provides methods to resolve and execute command handlers for various command
 * types.
 *
 * @author lengors
 */
@Service
public class CommandService {
  private static final Logger LOG = LoggerFactory.getLogger(CommandService.class);

  private final List<CommandHandlerResolver> commandHandlerResolvers;

  CommandService(final List<CommandHandlerResolver> commandHandlerResolvers) {
    this.commandHandlerResolvers = commandHandlerResolvers;
  }

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
    LOG.trace("Resolving handler for {command={}}", command);
    final var commandHandler = resolveCommandHandler(command);
    LOG.debug("Resolved handler for {command={}}: {}", command, commandHandler);

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
    if (LOG.isTraceEnabled()) {
      LOG.trace(
          "Executed {command={}, input={}, output={}} in {} ms",
          command,
          input,
          output,
          stopWatch.getTotalTimeMillis());
    } else if (LOG.isDebugEnabled()) {
      LOG.debug("Executed {command={}, input={}} in {} ms", command, input, stopWatch.getTotalTimeMillis());
    } else {
      LOG.info("Executed {command={}} in {} ms", command, stopWatch.getTotalTimeMillis());
    }
  }
}
