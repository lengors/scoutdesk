package io.github.lengors.scoutdesk.domain.reflect;

import java.util.Comparator;

/**
 * A comparator for determining the relative priority of classes based on their assignability relationship.
 * <p>
 * This comparator compares two classes and determines their order as follows:
 * <ul>
 *   <li>If the first class is assignable from the second class, it is ordered before the second class.</li>
 *   <li>If the second class is assignable from the first class, it is ordered after the first class.</li>
 *   <li>If neither class is assignable from the other, or if they are the same class, they are considered equal in
 *   terms of order.</li>
 * </ul>
 * <p>
 * This functionality is often useful in scenarios where the order of types needs to be resolved for tasks such as
 * prioritizing or determining compatibility.
 *
 * @author lengors
 */
public final class TypeComparator implements Comparator<Class<?>> {
  /**
   * Singleton instance of the {@link TypeComparator} class.
   * <p>
   * This instance provides a ready-to-use {@code Comparator} for comparing classes based on their assignability
   * relationship. It can be used directly wherever type comparison logic is required, ensuring consistent and efficient
   * comparisons across the application.
   */
  public static final TypeComparator INSTANCE = new TypeComparator();

  private TypeComparator() {
    // Empty constructor for protecting against unwanted instantiation
  }

  @Override
  public int compare(
    final Class<?> leftType,
    final Class<?> rightType
  ) {
    final var leftIsAssignableFromRight = leftType.isAssignableFrom(rightType);
    final var rightIsAssignableFromLeft = rightType.isAssignableFrom(leftType);

    if (leftIsAssignableFromRight && !rightIsAssignableFromLeft) {
      // left is a superclass of right
      return -1;
    }

    if (rightIsAssignableFromLeft && !leftIsAssignableFromRight) {
      // right is a superclass of left
      return 1;
    }

    // both are the same class or interfaces, or neither is a superclass of the other
    return 0;
  }
}
