package com.mofumo.api.services;

import com.mofumo.api.dtos.AddNewPetRequest;
import com.mofumo.api.dtos.PetDto;
import com.mofumo.api.dtos.UpdatePetRequest;
import com.mofumo.api.mappers.PetMapper;
import com.mofumo.api.mappers.UserMapper;
import com.mofumo.api.repositories.PetRepository;
import com.mofumo.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PetService {
  private final PetRepository petRepository;
  private final PetMapper petMapper;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public PetDto createPet(AddNewPetRequest request, Long userId) {
    var pet = petMapper.toEntity(request);
    var user = userRepository.findById(userId).orElseThrow();
    user.addPet(pet);
    petRepository.save(pet);
    return petMapper.toDto(pet);
  }

  public Set<PetDto> getAllPets(Long userId) {
    var user = userRepository.findById(userId).orElseThrow();
    return user.getPets().stream().map(petMapper::toDto).collect(Collectors.toSet());
  }

  public PetDto getPet(Long userId, Long petId) {
    var user = userRepository.findById(userId).orElseThrow();
    var pet = petRepository.findById(petId).orElseThrow();
    return petMapper.toDto(pet);
  }

  public PetDto updatePet(UpdatePetRequest request, Long userId, Long petId) {
    var pet = petRepository.findById(petId).orElseThrow();
    var user = userRepository.findById(userId).orElseThrow();
    petMapper.update(pet, request);
    petRepository.save(pet);
    return petMapper.toDto(pet);
  }

  public void removePet(Long userId, Long petId) {
    var user = userRepository.findById(userId).orElseThrow();
    var pet = petRepository.findById(petId).orElseThrow();
    user.removePet(pet);
    petRepository.delete(pet);
  }
}
