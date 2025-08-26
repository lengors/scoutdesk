package io.github.lengors.scoutdesk.api.advices;

import io.github.lengors.scoutdesk.domain.commands.CommandException;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.ErrorResponse;
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
  ErrorResponse handleCommandException(final CommandException commandException) {
    final var underlyingCause = commandException.getUnderlyingCause();
    if (underlyingCause == null) {
      throw commandException;
    }

    final var errorResponse = conversionService.convert(underlyingCause, ErrorResponse.class);
    if (errorResponse == null) {
      throw underlyingCause;
    }

    return errorResponse;
  }
}
