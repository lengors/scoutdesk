package io.github.lengors.scoutdesk.domain.io.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lengors.scoutdesk.domain.reflect.TypeComparator;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * A comparator implementation that determines the order of {@link ObjectMapper} instances based on the priority of
 * their associated {@link JsonFactory} subclasses.
 * <p>
 * This class is used to specify a custom priority for {@link ObjectMapper} instances derived from their factory types.
 * The priority is defined by a list of {@link JsonFactory} subclasses provided at instantiation.
 *
 * @author lengors
 */
public class ObjectMapperFactoryPrioritySpecification implements LoaderPrioritySpecification<ObjectMapper> {
  private static final Comparator<Class<?>> FACTORY_COMPARATOR = Collections.reverseOrder(TypeComparator.INSTANCE);

  private final List<Class<? extends JsonFactory>> factoryPriorities;

  /**
   * Constructs a new instance of {@code ObjectMapperFactoryPrioritySpecification} with the specified priority order for
   * {@link JsonFactory} subclasses. The provided collection of factory priorities determines the order in which
   * {@link ObjectMapper}s are compared based on their associated {@link JsonFactory} types. If the provided collection
   * is empty, the default priority is set to {@link JsonFactory}.
   *
   * @param factoryPriorities a collection of {@link JsonFactory} subclasses to define the custom priority order for
   *                          {@link ObjectMapper} instances; may be empty to use the default priority
   */
  public ObjectMapperFactoryPrioritySpecification(final Collection<Class<? extends JsonFactory>> factoryPriorities) {
    this.factoryPriorities = factoryPriorities.isEmpty()
      ? List.of(JsonFactory.class)
      : List.copyOf(factoryPriorities);
  }

  /**
   * Compares two {@link ObjectMapper} instances based on the priority of their associated {@link JsonFactory}
   * subclasses. The priority is determined by a predefined list of {@link JsonFactory} subclasses.
   *
   * @param leftMapper  the first {@link ObjectMapper} to compare
   * @param rightMapper the second {@link ObjectMapper} to compare
   * @return a negative integer, zero, or a positive integer as the priority of the {@code leftMapper}'s factory is less
   * than, equal to, or greater than the priority of the {@code rightMapper}'s factory, respectively
   */
  @Override
  public int compare(
    final ObjectMapper leftMapper,
    final ObjectMapper rightMapper
  ) {
    return Integer.compare(findFactoryPriority(leftMapper), findFactoryPriority(rightMapper));
  }

  /**
   * Tests whether the given {@link ObjectMapper} can be associated with a factory type that matches the predefined
   * priority criteria.
   *
   * @param mapper the {@link ObjectMapper} to be tested for the presence of a matching factory type
   * @return {@code true} if a matching factory type is found; {@code false} otherwise
   */
  @Override
  public boolean test(final ObjectMapper mapper) {
    return findFactoryType(mapper).isPresent();
  }

  private int findFactoryPriority(final ObjectMapper mapper) {
    final var foundFactoryType = findFactoryType(mapper).orElseThrow();
    return factoryPriorities.indexOf(foundFactoryType);
  }

  private Optional<Class<? extends JsonFactory>> findFactoryType(final ObjectMapper mapper) {
    final var factory = mapper.getFactory();
    return factoryPriorities
      .stream()
      .filter(factoryType -> factoryType.isInstance(factory))
      .min(FACTORY_COMPARATOR);
  }
}
