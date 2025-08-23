package io.github.lengors.scoutdesk.api.exceptions.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
class ExceptionHandlerRestControllerAdvice {
  private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerRestControllerAdvice.class);

  private final ConversionService conversionService;

  ExceptionHandlerRestControllerAdvice(final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @ExceptionHandler
  ErrorResponse handleThrowable(final Throwable throwable) throws Throwable {
    final var errorResponse = conversionService.convert(throwable, ErrorResponse.class);
    if (errorResponse == null) {
      throw throwable;
    }
    LOG.error("Emitting error response: {}", errorResponse, throwable);
    return errorResponse;
  }
}
