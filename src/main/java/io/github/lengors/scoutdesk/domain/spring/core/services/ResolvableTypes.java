package io.github.lengors.scoutdesk.domain.spring.core.services;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.core.ResolvableType;

/**
 * Utility class for working with Spring's {@link ResolvableType}.
 *
 * Provides methods to compare and flatten type hierarchies.
 *
 * @author lengors
 */
public final class ResolvableTypes {
  private ResolvableTypes() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Compares two {@link ResolvableType} instances.
   *
   * @param left  The left type
   * @param right The right type
   * @return A negative integer, zero, or a positive integer as the left type is
   *         less than, equal to, or greater than the right type
   */
  public static int compare(final ResolvableType left, final ResolvableType right) {
    if (left.isAssignableFrom(right) && right.isAssignableFrom(left)) {
      return 0;
    } else if (left.isAssignableFrom(right)) {
      return 1;
    } else if (right.isAssignableFrom(left)) {
      return -1;
    } else {
      return 0;
    }
  }

  /**
   * Flattens the type hierarchy of a {@link ResolvableType} into a stream.
   *
   * @param implementation The type to flatten
   * @return A stream of {@link ResolvableType} instances representing the type
   *         hierarchy
   */
  public static Stream<ResolvableType> flat(final ResolvableType implementation) {
    if (Objects.equals(implementation, ResolvableType.NONE)) {
      return Stream.empty();
    }

    var output = Stream.of(implementation);
    output = Stream.concat(output, flat(implementation.getSuperType()));
    return Stream.concat(output, Arrays
        .stream(implementation.getInterfaces())
        .flatMap(ResolvableTypes::flat));
  }
}
