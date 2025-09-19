package com.mofumo.api.controllers;

import com.mofumo.api.dtos.ProviderDto;
import com.mofumo.api.services.ProviderService;
import com.mofumo.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/providers")
public class ProviderController {
  private final UserService userService;
  private final ProviderService providerService;

  public ProviderController(UserService userService, ProviderService providerService) {
    this.userService = userService;
    this.providerService = providerService;
  }

  @PostMapping
  public ResponseEntity<ProviderDto> registerProvider(
          @Valid @RequestBody RegisterProviderRequest request,
          UriComponentsBuilder uriBuilder
  ){
    var providerDto = providerService.registerProvider(request);
    var uri = uriBuilder.path("/providers").build().toUri();
    return ResponseEntity.created(uri).body(providerDto);
  }
}
