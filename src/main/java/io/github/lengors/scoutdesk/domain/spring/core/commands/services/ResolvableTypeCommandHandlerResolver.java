package io.github.lengors.scoutdesk.domain.spring.core.commands.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;

import io.github.lengors.scoutdesk.domain.commands.models.Command;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandler;
import io.github.lengors.scoutdesk.domain.commands.services.CommandHandlerResolver;
import io.github.lengors.scoutdesk.domain.spring.core.services.ResolvableTypes;

/**
 * Resolves command handlers based on the command type using
 * {@link ResolvableType}.
 *
 * This class implements the {@link CommandHandlerResolver} interface and
 * provides a mechanism to resolve command handlers for specific command types.
 *
 * @author lengors
 */
@Service
class ResolvableTypeCommandHandlerResolver implements CommandHandlerResolver {
  private static final Logger LOG = LoggerFactory.getLogger(ResolvableTypeCommandHandlerResolver.class);

  @SuppressWarnings("LineLength")
  private final Map<ResolvableType, Optional<CommandHandler<?, ?, ?>>> cachedCommandHandlers = new ConcurrentHashMap<>();
  private final List<? extends Pair<ResolvableType, ? extends CommandHandler<?, ?, ?>>> commandHandlers;

  ResolvableTypeCommandHandlerResolver(final List<CommandHandler<?, ?, ?>> commandHandlers) {
    this.commandHandlers = commandHandlers
        .stream()
        .map(commandHandler -> Pair.of(
            ResolvableTypes
                .flat(ResolvableType.forInstance(commandHandler))
                .filter(CommandHandler.RESOLVABLE_TYPE::isAssignableFrom)
                .map(type -> Arrays
                    .stream(type.getGenerics())
                    .findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(ResolvableTypes::compare)
                .findFirst()
                .get(),
            commandHandler))
        .sorted((left, right) -> ResolvableTypes.compare(left.getLeft(), right.getLeft()))
        .peek(pair -> LOG.debug("Command handler {} resolving to {}", pair.getLeft(), pair.getRight()))
        .toList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <C extends Command<I, O>, I, O> @Nullable CommandHandler<C, I, O> resolve(final C command) {
    return (@Nullable CommandHandler<C, I, O>) cachedCommandHandlers
        .computeIfAbsent(ResolvableType.forInstance(command), type -> commandHandlers
            .stream()
            .filter(pair -> pair
                .getLeft()
                .isAssignableFrom(type))
            .findFirst()
            .map(Pair::getRight))
        .orElse(null);
  }
}
