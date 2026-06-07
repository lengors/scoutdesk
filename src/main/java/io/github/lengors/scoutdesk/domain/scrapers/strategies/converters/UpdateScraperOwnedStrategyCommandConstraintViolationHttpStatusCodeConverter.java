package io.github.lengors.scoutdesk.domain.scrapers.strategies.converters;

import io.github.lengors.scoutdesk.domain.persistence.constraints.RequireEntity;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.commands.UpdateScraperOwnedStrategyCommand;
import io.github.lengors.scoutdesk.domain.scrapers.strategies.models.ScraperNamedStrategy;
import io.github.lengors.scoutdesk.domain.spring.core.converters.AbstractConstraintViolationHttpStatusCodeConditionalConverter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
class UpdateScraperOwnedStrategyCommandConstraintViolationHttpStatusCodeConverter
  extends AbstractConstraintViolationHttpStatusCodeConditionalConverter<UpdateScraperOwnedStrategyCommand.Handler> {
  UpdateScraperOwnedStrategyCommandConstraintViolationHttpStatusCodeConverter() {
    super(UpdateScraperOwnedStrategyCommand.Handler.class);
  }

  @Override
  public HttpStatus convert(final @NotNull ConstraintViolation<UpdateScraperOwnedStrategyCommand.Handler> source) {
    if (source.getInvalidValue() instanceof ScraperNamedStrategy && RequireEntity.class.equals(source
      .getConstraintDescriptor()
      .getAnnotation()
      .annotationType())) {
      return HttpStatus.NOT_FOUND;
    }

    return HttpStatus.BAD_REQUEST;
  }
}
