package com.mofumo.api.controllers;

import com.mofumo.api.dtos.AddNewPetRequest;
import com.mofumo.api.dtos.PetDto;
import com.mofumo.api.mappers.PetMapper;
import com.mofumo.api.repositories.PetRepository;
import com.mofumo.api.services.PetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/{userId}/pets")
@AllArgsConstructor
public class PetController {
  private final PetMapper petMapper;
  private final PetRepository petRepository;
  private final PetService petService;

  @PostMapping
  @PreAuthorize("#userId.toString() == authentication.name and hasRole('CUSTOMER')")
  public ResponseEntity<PetDto> addPet(
          @RequestBody AddNewPetRequest request,
          @PathVariable Long userId,
          UriComponentsBuilder uriBuilder
  ) {
    var petDto =  petService.createPet(request, userId);
    var uri = uriBuilder.path("/{petId}").buildAndExpand(petDto.getId()).toUri();
    return ResponseEntity.created(uri).body(petDto);
  }
}
