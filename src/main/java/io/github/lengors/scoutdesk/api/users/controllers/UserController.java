package io.github.lengors.scoutdesk.api.users.controllers;

import io.github.lengors.scoutdesk.domain.spring.security.models.User;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.util.NullnessUtil;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('USER')")
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.PARAMETER)
@RequestMapping({"/api/v1/users", "/api/users"})
class UserController {
  private final ConversionService conversionService;

  UserController(final @NotNull ConversionService conversionService) {
    this.conversionService = conversionService;
  }

  @GetMapping("/me")
  User findUser(@AuthenticationPrincipal final @NotNull AuthenticatedPrincipal authenticatedPrincipal) {
    return NullnessUtil.castNonNull(
      conversionService.convert(authenticatedPrincipal, NullnessUtil.castNonNull(User.class)));
  }
}
