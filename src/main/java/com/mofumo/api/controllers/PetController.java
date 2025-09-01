package com.mofumo.api.controllers;

import com.mofumo.api.dtos.AddNewPetRequest;
import com.mofumo.api.dtos.PetDto;
import com.mofumo.api.mappers.PetMapper;
import com.mofumo.api.repositories.PetRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{userId}/pets")
@AllArgsConstructor
public class PetController {
  private final PetMapper petMapper;
  private final PetRepository petRepository;


  @PostMapping
  @PostAuthorize("#userId.toString() == authentication.name")
  public ResponseEntity<PetDto> addPet(@RequestBody AddNewPetRequest request, @PathVariable Long userId) {
    var pet = petMapper.toEntity(request);
    petRepository.save(pet);
    return ResponseEntity.ok(petMapper.toDto(pet));
  }
}
