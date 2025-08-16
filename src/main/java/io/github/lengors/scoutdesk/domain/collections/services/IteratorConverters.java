package io.github.lengors.scoutdesk.domain.collections.services;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Utility class for converting an {@link Iterator} to an {@link Enumeration}. This is useful when you need to work with
 * APIs that require an {@link Enumeration} but you have an {@link Iterator} available.
 *
 * @author lengors
 */
public final class IteratorConverters {
  private IteratorConverters() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Converts an {@link Iterator} to an {@link Enumeration}.
   * <p>
   * This method provides a way to adapt an {@link Iterator} to the {@link Enumeration} interface, allowing you to use
   * the iterator in contexts that require an enumeration.
   *
   * @param iterator the iterator to convert
   * @param <T>      the type of elements returned by the iterator
   * @return an enumeration that wraps the provided iterator
   */
  public static <T> Enumeration<T> toEnumeration(final Iterator<T> iterator) {
    return new Enumeration<>() {
      @Override
      public boolean hasMoreElements() {
        return iterator.hasNext();
      }

      @Override
      public T nextElement() {
        return iterator.next();
      }
    };
  }
}
