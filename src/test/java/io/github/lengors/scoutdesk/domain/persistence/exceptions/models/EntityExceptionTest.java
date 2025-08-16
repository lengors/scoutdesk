package io.github.lengors.scoutdesk.domain.persistence.exceptions.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import org.testcontainers.shaded.org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.BiFunction;

class EntityExceptionTest {
  @SuppressWarnings("unused")
  private static final List<EntityExceptionFactory> TO_STRING_FACTORIES =
    List.of(EntityDeleteException::new, EntityFindException::new, EntitySaveException::new);

  private static final List<Pair<EntityExceptionFactory, EntityExceptionFactory>> EQUALS_FACTORIES;

  static {
    EQUALS_FACTORIES = TO_STRING_FACTORIES
      .stream()
      .flatMap(leftFactory -> TO_STRING_FACTORIES
        .stream()
        .map(rightFactory -> Pair.of(leftFactory, rightFactory)))
      .toList();
  }

  @ParameterizedTest
  @FieldSource("TO_STRING_FACTORIES")
  void testEntityExceptionToString(
    final BiFunction<Class<?>, String, ? extends EntityException> entityExceptionFactory
  ) {
    final var entityDeleteException = entityExceptionFactory.apply(Object.class, "TEST");
    Assertions.assertEquals(
      "%s{runtimeType=Object, query=TEST}".formatted(entityDeleteException
        .getClass()
        .getSimpleName()),
      entityDeleteException.toString());
  }

  @ParameterizedTest
  @FieldSource("EQUALS_FACTORIES")
  void testEntityExceptionEquals(final Pair<EntityExceptionFactory, EntityExceptionFactory> factories) {
    final var leftEntityException = factories
      .getLeft()
      .apply(Object.class, "TEST");
    final var rightEntityException = factories
      .getRight()
      .apply(Object.class, "TEST");
    if (leftEntityException.getClass() != rightEntityException.getClass()) {
      Assertions.assertNotEquals(leftEntityException, rightEntityException);
    } else {
      Assertions.assertEquals(leftEntityException, rightEntityException);
    }
  }
}
