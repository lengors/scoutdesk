package io.github.lengors.scoutdesk.domain.scrapers.strategies.converters;

import io.github.lengors.scoutdesk.domain.errors.AbstractConstraintViolationHttpStatusConditionalConverter;
import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.SaveScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperNamedStrategy;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
final class SaveScraperOwnedStrategyCommandConstraintViolationHttpStatusConverter
  extends AbstractConstraintViolationHttpStatusConditionalConverter<SaveScraperOwnedStrategyCommand.Handler> {
  SaveScraperOwnedStrategyCommandConstraintViolationHttpStatusConverter() {
    super(SaveScraperOwnedStrategyCommand.Handler.class);
  }

  @Override
  public HttpStatus convert(final @NotNull ConstraintViolation<SaveScraperOwnedStrategyCommand.Handler> source) {
    if (source.getInvalidValue() instanceof ScraperNamedStrategy && RequireEntity.class.equals(source
      .getConstraintDescriptor()
      .getAnnotation()
      .annotationType())) {
      return HttpStatus.CONFLICT;
    }

    return HttpStatus.BAD_REQUEST;
  }
}
