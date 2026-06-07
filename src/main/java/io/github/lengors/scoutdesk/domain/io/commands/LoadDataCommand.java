package io.github.lengors.scoutdesk.domain.io.commands;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lengors.scoutdesk.domain.commands.Command;
import io.github.lengors.scoutdesk.domain.commands.CommandHandler;
import io.github.lengors.scoutdesk.domain.functional.ThrowingFunction;
import io.github.lengors.scoutdesk.domain.io.exceptions.LoadDataFormatException;
import io.github.lengors.scoutdesk.domain.io.exceptions.LoadDataIOException;
import io.github.lengors.scoutdesk.domain.io.services.LoaderPrioritySpecification;
import io.github.lengors.scoutdesk.domain.io.services.ObjectMapperFactoryPrioritySpecification;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * A command for loading data with an input type and output type specification. The data is loaded through a provided
 * function that generates an {@link InputStream} and optionally applies a priority specification for
 * {@link ObjectMapper} instances.
 *
 * @param type                        the output type of the command
 * @param loaderGenerator             a function that generates an {@link InputStream} from the input type
 * @param loaderPrioritySpecification an optional priority specification for {@link ObjectMapper} instances
 * @param <I>                         the input type of the command
 * @param <O>                         the output type of the command
 * @author lengors
 */
public record LoadDataCommand<I extends @NotNull Object, O>(
  Class<O> type,
  ThrowingFunction<I, InputStream, IOException> loaderGenerator,
  @Nullable LoaderPrioritySpecification<? super ObjectMapper> loaderPrioritySpecification
) implements Command<I, O> {
  private static final Logger LOG = LoggerFactory.getLogger(LoadDataCommand.class);

  /**
   * Constructs a new instance of the {@code LoadDataCommand} class.
   *
   * @param type            the target type into which data should be loaded
   * @param loaderGenerator a function that generates an {@link InputStream} from the given input, which may throw an
   *                        {@link IOException}
   */
  public LoadDataCommand(
    final Class<O> type,
    final ThrowingFunction<I, InputStream, IOException> loaderGenerator
  ) {
    this(type, loaderGenerator, (@Nullable LoaderPrioritySpecification<? super ObjectMapper>) null);
  }

  /**
   * Constructs a new instance of the {@code LoadDataCommand} class.
   *
   * @param type              the target type into which data should be loaded
   * @param loaderGenerator   a function that provides an {@link InputStream} from the given input, capable of throwing
   *                          an {@link IOException}
   * @param factoryPriorities a collection of {@link JsonFactory} subclasses defining the priority order for data
   *                          processing
   */
  public LoadDataCommand(
    final Class<O> type,
    final ThrowingFunction<I, InputStream, IOException> loaderGenerator,
    final Collection<Class<? extends JsonFactory>> factoryPriorities
  ) {
    this(type, loaderGenerator, new ObjectMapperFactoryPrioritySpecification(factoryPriorities));
  }

  @Service
  static class Handler<I extends @NotNull Object, O> implements CommandHandler<LoadDataCommand<I, O>, I, O> {
    private final List<ObjectMapper> objectMappers;

    Handler(final List<ObjectMapper> objectMappers) {
      this.objectMappers = objectMappers;
    }

    @Override
    public O handle(
      final LoadDataCommand<I, O> command,
      final I input
    ) {
      final var loaderPrioritySpecification = command.loaderPrioritySpecification();
      final var mappers = loaderPrioritySpecification == null
        ? this.objectMappers
        : this.objectMappers
          .stream()
          .filter(loaderPrioritySpecification)
          .sorted(loaderPrioritySpecification)
          .toList();

      final InputStream inputStream;
      try {
        inputStream = command
          .loaderGenerator()
          .apply(input);
      } catch (final IOException exception) {
        LOG.error("Failed with IO exception", exception);
        throw new LoadDataIOException(input, exception);
      }

      for (final var mapper : mappers) {
        final JsonNode tree;
        try {
          tree = mapper.readTree(inputStream);
        } catch (final StreamReadException exception) {
          LOG.trace(
            "Failed to read the specification file with {objectMapper={}, factory={}}",
            mapper
              .getClass()
              .getSimpleName(),
            mapper
              .getFactory()
              .getClass()
              .getSimpleName(),
            exception
          );
          continue;
        } catch (final IOException exception) {
          throw new LoadDataIOException(input, exception);
        }

        if (tree != null) {
          final O value;
          try {
            value = mapper.treeToValue(tree, command.type());
          } catch (final JsonProcessingException exception) {
            throw new LoadDataFormatException(input, exception);
          } catch (final IllegalArgumentException exception) {
            throw new LoadDataFormatException(input, exception);
          }
          return value;
        }
      }

      throw new LoadDataFormatException(input);
    }
  }
}
