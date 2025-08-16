package io.github.lengors.scoutdesk.domain.spring.core.conditions.services;

import io.github.lengors.scoutdesk.domain.spring.core.conditions.models.ConditionalOnNonBlankProperty;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * A condition that checks if a specific property is defined and not blank. This condition is used in conjunction with
 * the {@link ConditionalOnNonBlankProperty} annotation.
 * <p>
 * It evaluates whether the specified property is present in the environment and has a non-blank value. If the property
 * is not defined or is blank, the condition will not match.
 * <p>
 * This condition is typically used to control the activation of beans based on the presence and value of a specific
 * property in the application's environment.
 *
 * @author lengors
 */
public final class NonBlankPropertyCondition implements Condition {

  /**
   * Default constructor for the condition. It is required for Spring to instantiate this condition.
   */
  public NonBlankPropertyCondition() {
    // Empty constructor for Spring to instantiate this condition.
  }

  @Override
  public boolean matches(
    final @NotNull ConditionContext context,
    final @NotNull AnnotatedTypeMetadata metadata
  ) {
    if (!metadata.isAnnotated(ConditionalOnNonBlankProperty.class.getName())) {
      return false;
    }

    final var annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnNonBlankProperty.class.getName());
    final var propertyName = annotationAttributes != null
      ? (String) annotationAttributes.get("value")
      : null;

    if (propertyName == null) {
      return false;
    }

    final var property = context
      .getEnvironment()
      .getProperty(propertyName);

    return property != null && !property.isBlank();
  }
}
