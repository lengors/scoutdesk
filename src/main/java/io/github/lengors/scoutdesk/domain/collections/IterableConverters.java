package io.github.lengors.scoutdesk.domain.collections;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for converting iterables to other types.
 * <p>
 * This class provides a method to convert an iterable to a set. If the iterable is already a set, it is returned as is.
 * Otherwise, a new HashSet is created and populated with the elements of the iterable.
 *
 * @author lengors
 */
public final class IterableConverters {

  private IterableConverters() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Converts an iterable to an enumeration. This method uses the
   * {@link IteratorConverters#toEnumeration(java.util.Iterator)} method to perform the conversion.
   *
   * @param <T>      The type of elements in the iterable
   * @param iterable The iterable to convert
   * @return An enumeration containing the elements of the iterable
   */
  public static <T> Enumeration<T> toEnumeration(final Iterable<T> iterable) {
    return IteratorConverters.toEnumeration(iterable.iterator());
  }

  /**
   * Converts an iterable to a set. If the iterable is already a set, it is returned as is. Otherwise, a new HashSet is
   * created and populated with the elements of the iterable.
   *
   * @param <T>      The type of elements in the iterable
   * @param iterable The iterable to convert
   * @return A set containing the elements of the iterable
   */
  public static <T> Set<T> toSet(final Iterable<T> iterable) {
    final Set<T> output;
    if (iterable instanceof Set<T> set) {
      output = set;
    } else {
      output = new HashSet<>();
      iterable.forEach(output::add);
    }
    return output;
  }
}
