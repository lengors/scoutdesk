package io.github.lengors.scoutdesk.domain.scrapers.strategies.converters;

import io.github.lengors.scoutdesk.domain.errors.AbstractConstraintViolationHttpStatusConditionalConverter;
import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.UpdateScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperNamedStrategy;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
final class UpdateScraperOwnedStrategyCommandConstraintViolationHttpStatusConverter
  extends AbstractConstraintViolationHttpStatusConditionalConverter<UpdateScraperOwnedStrategyCommand.Handler> {
  UpdateScraperOwnedStrategyCommandConstraintViolationHttpStatusConverter() {
    super(UpdateScraperOwnedStrategyCommand.Handler.class);
  }

  @Override
  public HttpStatus convert(final ConstraintViolation<UpdateScraperOwnedStrategyCommand.Handler> source) {
    if (source.getInvalidValue() instanceof ScraperNamedStrategy && RequireEntity.class.equals(source
      .getConstraintDescriptor()
      .getAnnotation()
      .annotationType())) {
      return HttpStatus.NOT_FOUND;
    }

    return HttpStatus.BAD_REQUEST;
  }
}
