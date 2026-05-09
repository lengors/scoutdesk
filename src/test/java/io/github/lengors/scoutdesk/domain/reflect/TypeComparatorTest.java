package io.github.lengors.scoutdesk.domain.reflect;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TypeComparatorTest {
  @Test
  void givenMoreSpecificTypeWhenComparingWithLessSpecificTypeThenComparatorEvaluatesCorrectly() {
    final var order = TypeComparator.INSTANCE.compare(YAMLFactory.class, JsonFactory.class);
    Assertions.assertEquals(1, order);
  }

  @Test
  void givenLessSpecificTypeWhenComparingWithMoreSpecificTypeThenComparatorEvaluatesCorrectly() {
    final var order = TypeComparator.INSTANCE.compare(JsonFactory.class, YAMLFactory.class);
    Assertions.assertEquals(-1, order);
  }

  @Test
  void givenTypeFromDifferentTypeHierarchyWhenComparingWithOtherTypeThenComparatorEvaluatesCorrectly() {
    final var order = TypeComparator.INSTANCE.compare(JsonFactory.class, String.class);
    Assertions.assertEquals(0, order);
  }

  @Test
  void givenTypeFromSameTypeHierarchyWhenComparingWithOtherTypeThenComparatorEvaluatesCorrectly() {
    final var order = TypeComparator.INSTANCE.compare(JsonFactory.class, JsonFactory.class);
    Assertions.assertEquals(0, order);
  }
}
