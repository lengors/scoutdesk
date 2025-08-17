package io.github.lengors.scoutdesk.api.exceptions.controllers;

import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntityDeleteException;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntityFindException;
import io.github.lengors.scoutdesk.domain.persistence.exceptions.models.EntitySaveException;
import io.github.lengors.scoutdesk.domain.scrapers.specifications.exceptions.models.ScraperOwnedSpecificationStatusTransitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;

import java.util.Objects;

@ControllerAdvice(annotations = RestController.class)
class ExceptionHandlerRestControllerAdvice {
  private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerRestControllerAdvice.class);

  @ExceptionHandler
  ErrorResponse handleEntityDeleteException(final EntityDeleteException entityDeleteException) {
    LOG.error("Failed to delete entity", entityDeleteException);
    return ErrorResponse.create(
      entityDeleteException,
      HttpStatus.CONFLICT,
      "Entity {type=%s} cannot be deleted because at least one of {query=%s} depends on it".formatted(
        entityDeleteException.getRuntimeTypeName(),
        Objects.requireNonNullElse(entityDeleteException.getQuery(), "null")
      ));
  }

  @ExceptionHandler
  ErrorResponse handleEntityFindException(final EntityFindException entityFindException) {
    LOG.error("Failed to find entity", entityFindException);
    return ErrorResponse.create(
      entityFindException,
      HttpStatus.NOT_FOUND,
      "Entity {type=%s} not found for {query=%s}".formatted(
        entityFindException.getRuntimeTypeName(),
        Objects.requireNonNullElse(entityFindException.getQuery(), "null")
      )
    );
  }

  @ExceptionHandler
  ErrorResponse handleEntitySaveException(final EntitySaveException entitySaveException) {
    LOG.error("Failed to save entity", entitySaveException);
    return ErrorResponse.create(
      entitySaveException,
      HttpStatus.CONFLICT,
      "Entity {type=%s} already exists for {query=%s}".formatted(
        entitySaveException.getRuntimeTypeName(),
        Objects.requireNonNullElse(entitySaveException.getQuery(), "null")
      ));
  }

  @ExceptionHandler
  ErrorResponse handleRestClientResponseException(final RestClientResponseException restClientResponseException) {
    LOG.error("Failed to connect to service", restClientResponseException);
    return ErrorResponse.create(
      restClientResponseException,
      restClientResponseException.getStatusCode(),
      restClientResponseException.getResponseBodyAsString()
    );
  }

  @ExceptionHandler
  ErrorResponse handleScraperOwnedSpecificationStatusTransitionException(
    final ScraperOwnedSpecificationStatusTransitionException scraperOwnedSpecificationStatusTransitionException
  ) {
    LOG.error("Failed to transition scraper owned specification status",
      scraperOwnedSpecificationStatusTransitionException);
    return ErrorResponse.create(
      scraperOwnedSpecificationStatusTransitionException,
      HttpStatus.UNPROCESSABLE_ENTITY,
      "Invalid status transition from %s to %s".formatted(
        scraperOwnedSpecificationStatusTransitionException.getFrom(),
        scraperOwnedSpecificationStatusTransitionException.getTo()
      )
    );
  }
}
