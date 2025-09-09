package com.mofumo.api.controllers;

import com.mofumo.api.dtos.AddNewPetRequest;
import com.mofumo.api.dtos.PetDto;
import com.mofumo.api.dtos.UpdatePetRequest;
import com.mofumo.api.mappers.PetMapper;
import com.mofumo.api.repositories.PetRepository;
import com.mofumo.api.services.PetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

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

  @PreAuthorize("#userId.toString() == authentication.name and hasRole('CUSTOMER')")
  @GetMapping
  public ResponseEntity<Set<PetDto>> getAllPets(@PathVariable Long userId) {
    var pets = petService.getAllPets(userId);
    return ResponseEntity.ok().body(pets);
  }

  @PreAuthorize("#userId.toString() == authentication.name and hasRole('CUSTOMER')")
  @GetMapping("/{petId}")
  public ResponseEntity<PetDto> getPet(@PathVariable Long userId, @PathVariable Long petId) {
    var petDto = petService.getPet(userId, petId);
    return ResponseEntity.ok().body(petDto);
  }

  @PreAuthorize("#userId.toString() == authentication.name and hasRole('CUSTOMER')")
  @PostMapping("/{petId}")
  public ResponseEntity<PetDto> updatePet(
          @PathVariable Long userId,
          @PathVariable Long petId,
          @RequestBody UpdatePetRequest request
          ) {
    var petDto = petService.updatePet(request, userId, petId);
    return ResponseEntity.ok(petDto);
  }

  @DeleteMapping("/{petId}")
  @PreAuthorize("#userId.toString() == authentication.name and hasRole('CUSTOMER')")
  public ResponseEntity<Void> removePet(@PathVariable Long userId, @PathVariable Long petId){
    petService.removePet(userId, petId);
    return ResponseEntity.noContent().build();
  }
}
