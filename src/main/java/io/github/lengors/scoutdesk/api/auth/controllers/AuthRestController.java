package io.github.lengors.scoutdesk.api.auth.controllers;

import io.github.lengors.scoutdesk.api.auth.exceptions.InvalidRedirectException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.PARAMETER)
@RequestMapping("/auth")
class AuthRestController {
  @GetMapping("/challenge")
  void challenge(
    @RequestParam(defaultValue = "/", name = "redirect") final @NotNull String redirect,
    final @NotNull HttpServletResponse response
  ) throws IOException {
    if (!redirect.startsWith("/") || redirect.startsWith("//")) {
      throw new InvalidRedirectException(redirect);
    }

    response.sendRedirect(redirect);
  }
}
