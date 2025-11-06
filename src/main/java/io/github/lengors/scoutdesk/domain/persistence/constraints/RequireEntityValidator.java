package io.github.lengors.scoutdesk.domain.persistence.constraints;

import io.github.lengors.scoutdesk.domain.persistence.exceptions.EntityNotFoundException;
import io.github.lengors.scoutdesk.domain.persistence.models.EntityReferrer;
import io.github.lengors.scoutdesk.domain.persistence.services.EntityService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.springframework.core.convert.ConversionService;

class RequireEntityValidator implements ConstraintValidator<RequireEntity, Object> {
  private final ConversionService conversionService;
  private final EntityService entityService;

  @SuppressWarnings("unchecked")
  private Class<? extends EntityReferrer<?>> referrerType =
    (Class<? extends EntityReferrer<?>>) NullnessUtil.castNonNull((Object) EntityReferrer.class);
  private boolean absent = RequireEntity.DEFAULT_ABSENT_VALUE;
  private String property = StringUtils.EMPTY;

  RequireEntityValidator(
    final ConversionService conversionService,
    final EntityService entityService
  ) {
    this.conversionService = conversionService;
    this.entityService = entityService;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void initialize(final RequireEntity constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    referrerType = (Class<? extends EntityReferrer<?>>) constraintAnnotation.referrerType();
    property = constraintAnnotation.property();
    absent = constraintAnnotation.absent();
  }

  @Override
  public boolean isValid(
    final Object value,
    final ConstraintValidatorContext context
  ) {
    final var referrer = referrerType.isInstance(value)
      ? referrerType.cast(value)
      : conversionService.convert(value, referrerType);

    if (referrer == null) {
      return true;
    }

    if (isValid(referrer)) {
      return true;
    }

    final var messageTemplate = getMessageTemplate(referrer, context);
    context.disableDefaultConstraintViolation();
    var constraintViolationBuilder = context.buildConstraintViolationWithTemplate(messageTemplate);
    if (StringUtils.isNotBlank(property)) {
      constraintViolationBuilder
        .addPropertyNode(property)
        .addConstraintViolation();
    } else {
      constraintViolationBuilder.addConstraintViolation();
    }
    return false;
  }

  private String getMessageTemplate(final EntityReferrer<?> referrer, final ConstraintValidatorContext context) {
    final String unformattedMessageTemplate;
    if (absent) {
      unformattedMessageTemplate = referrer.isIndividualEntity()
        ? "%s already exists"
        : "one or more %s already exist";
    } else {
      unformattedMessageTemplate = referrer.isIndividualEntity()
        ? "%s does not exist"
        : "one or more %s do not exist";
    }

    final var formattedMessageTemplate = unformattedMessageTemplate.formatted(referrer.getTypeName());
    return context
      .getDefaultConstraintMessageTemplate()
      .formatted(formattedMessageTemplate);
  }

  private boolean isValid(final EntityReferrer<?> referrer) {
    try {
      entityService.findEntity(referrer);
    } catch (final EntityNotFoundException exception) {
      return absent;
    }
    return !absent;
  }
}
