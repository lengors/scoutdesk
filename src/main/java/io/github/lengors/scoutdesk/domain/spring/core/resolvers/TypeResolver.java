package io.github.lengors.scoutdesk.domain.spring.core.resolvers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import io.github.lengors.scoutdesk.domain.resolvers.Resolver;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;

import io.github.lengors.scoutdesk.domain.spring.core.services.ResolvableTypes;

/**
 * A resolver that resolves an input object to an output object based on the type of the input object.
 *
 * @param <O> the output type
 */
public final class TypeResolver<O extends @NotNull Object> implements Resolver<@NotNull Object, O> {
  private static final Logger LOG = LoggerFactory.getLogger(TypeResolver.class);

  private final Map<ResolvableType, Optional<O>> cachedResolvables = new ConcurrentHashMap<>();
  private final List<? extends Pair<ResolvableType, ? extends O>> resolvables;

  /**
   * Creates a new {@link TypeResolver} instance.
   *
   * @param resolvables the list of resolvable objects
   */
  public TypeResolver(final List<O> resolvables) {
    final var targetResolvableType = ResolvableType
      .forInstance(resolvables)
      .getGeneric(0);
    this.resolvables = resolvables
      .stream()
      .map(resolvable -> Pair.of(
        ResolvableTypes
          .flat(ResolvableType.forInstance(resolvable))
          .filter(targetResolvableType::isAssignableFrom)
          .map(type -> Arrays
            .stream(type.getGenerics())
            .findFirst())
          .filter(Optional::isPresent)
          .map(Optional::get)
          .min(ResolvableTypes::compare)
          .orElseThrow(),
        resolvable))
      .sorted((left, right) -> ResolvableTypes.compare(left.getLeft(), right.getLeft()))
      .peek(pair -> LOG.debug("Resolvable {} resolving to {}", pair.getLeft(), pair.getRight()))
      .toList();
  }

  @Override
  public @Nullable O resolve(final @NotNull Object input) {
    return cachedResolvables
      .computeIfAbsent(ResolvableType.forInstance(input), type -> resolvables
        .stream()
        .filter(pair -> pair
          .getLeft()
          .isAssignableFrom(type))
        .findFirst()
        .map(Pair::getRight))
      .orElse(null);
  }
}
