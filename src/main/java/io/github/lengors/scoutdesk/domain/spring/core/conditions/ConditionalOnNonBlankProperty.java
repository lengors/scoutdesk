package io.github.lengors.scoutdesk.domain.spring.core.conditions;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to conditionally enable a bean based on the presence of a non-blank property. The
 * property is specified by the value of the annotation. If the property is present and not blank, the annotated bean
 * will be created.
 *
 * @author lengors
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(NonBlankPropertyCondition.class)
public @interface ConditionalOnNonBlankProperty {

  /**
   * The name of the property to check. If the property is present and not blank, the condition will match.
   *
   * @return the name of the property
   */
  String value();
}
