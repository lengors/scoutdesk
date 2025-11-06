package io.github.lengors.scoutdesk.domain.scrapers.profiles.constraints;

import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecification;
import io.github.lengors.protoscout.domain.scrapers.specifications.models.ScraperSpecificationRequirementType;
import io.github.lengors.scoutdesk.domain.commands.CommandService;
import io.github.lengors.scoutdesk.domain.scrapers.profiles.models.ScraperNamelessProfile;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.constraints.ScraperSpecificationCompliant;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.models.ScraperSpecificationRequirementBatch;
import io.github.lengors.scoutdesk.integrations.webscout.commands.FindScraperSpecificationCommand;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.client.RestClientResponseException;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * Validator to ensure that a {@link ScraperNamelessProfile} complies with its associated {@link ScraperSpecification}.
 * <p>
 * This validator checks that all required inputs defined in the specification are present and correctly formatted in
 * the profile.
 *
 * @author lengors
 */
public final class ScraperNamelessProfileSpecificationCompliantValidator
  implements ConstraintValidator<ScraperSpecificationCompliant, ScraperNamelessProfile> {
  private final ConversionService conversionService;
  private final CommandService commandService;
  private final EmailValidator emailValidator;

  ScraperNamelessProfileSpecificationCompliantValidator(
    final ConversionService conversionService,
    final CommandService commandService,
    final EmailValidator emailValidator
  ) {
    this.conversionService = conversionService;
    this.commandService = commandService;
    this.emailValidator = emailValidator;
  }

  @Override
  public boolean isValid(
    final ScraperNamelessProfile value,
    final ConstraintValidatorContext context
  ) {
    final ScraperSpecification scraperSpecification;
    try {
      scraperSpecification = commandService.executeCommand(
        new FindScraperSpecificationCommand(),
        value
          .specification()
          .fullyQualifiedName());
    } catch (final RestClientResponseException exception) {
      return true;
    }

    final var inputs = Objects.requireNonNullElseGet(value.inputs(), Collections::<String, String>emptyMap);
    final var requirementBatch = NullnessUtil
      .castNonNull(conversionService.convert(scraperSpecification, ScraperSpecificationRequirementBatch.class));

    boolean specificationCompliant = true;
    for (final var requirement : requirementBatch.requirements()) {
      final var requirementType = requirement.getType();
      final var input = Optional
        .ofNullable(inputs.get(requirement.getName()))
        .filter(StringUtils::isNotBlank)
        .or(() -> Optional
          .ofNullable(requirement.getDefault())
          .filter(StringUtils::isNotBlank))
        .orElse(null);

      final boolean isMissingInput;
      if (input == null) {
        isMissingInput = true;
      } else if (requirementType == ScraperSpecificationRequirementType.EMAIL && !emailValidator.isValid(input)) {
        isMissingInput = false;
      } else {
        continue;
      }

      if (specificationCompliant) {
        context.disableDefaultConstraintViolation();
        specificationCompliant = false;
      }

      final var formattedMessageTemplate = isMissingInput
        ? "%s is required".formatted(requirement.getName())
        : "invalid %s format".formatted(requirementType
          .name()
          .toLowerCase());
      final var messageTemplate = context
        .getDefaultConstraintMessageTemplate()
        .formatted(formattedMessageTemplate);
      context
        .buildConstraintViolationWithTemplate(messageTemplate)
        .addPropertyNode("inputs.%s".formatted(requirement.getName()))
        .addConstraintViolation();
    }

    return specificationCompliant;
  }
}
