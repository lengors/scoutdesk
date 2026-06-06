package io.github.lengors.scoutdesk.api.users.controllers;

import io.github.lengors.scoutdesk.domain.spring.security.models.User;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole(T(io.github.lengors.scoutdesk.domain.spring.security.models.UserRoleNames).USER_ALIAS)")
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.PARAMETER)
@RequestMapping({"/api/v1/users", "/api/users"})
class UserController {
  @GetMapping("/me")
  User findUser(final @NotNull User user) {
    return user;
  }
}
