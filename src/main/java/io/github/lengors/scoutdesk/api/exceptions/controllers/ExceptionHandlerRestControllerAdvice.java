package io.github.lengors.scoutdesk.api.exceptions.controllers;

import io.github.lengors.scoutdesk.domain.commands.CommandException;
import io.github.lengors.scoutdesk.domain.errors.ErrorReport;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
class ExceptionHandlerRestControllerAdvice {
  private final ConversionService conversionService;

  ExceptionHandlerRestControllerAdvice(final ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @ExceptionHandler
  ResponseEntity<?> handleThrowable(final Throwable throwable) throws Throwable {
    final var underlyingCause = throwable instanceof CommandException commandException
      ? commandException.getUnderlyingCause()
      : throwable;
    final var errorReport = underlyingCause != null
      ? conversionService.convert(underlyingCause, ErrorReport.class)
      : null;

    if (errorReport != null) {
      return buildResponseEntity(errorReport);
    }

    final var rootCause = underlyingCause != null
      ? underlyingCause
      : throwable;

    if (rootCause instanceof ErrorReport rootErrorReport) {
      return buildResponseEntity(rootErrorReport);
    }

    throw rootCause;
  }

  private static ResponseEntity<?> buildResponseEntity(final ErrorReport errorReport) {
    return ResponseEntity
      .status(errorReport.statusCode())
      .body(errorReport.body());
  }
}
