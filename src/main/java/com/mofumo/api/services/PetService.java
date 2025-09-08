package com.mofumo.api.services;

import com.mofumo.api.dtos.AddNewPetRequest;
import com.mofumo.api.dtos.PetDto;
import com.mofumo.api.mappers.PetMapper;
import com.mofumo.api.mappers.UserMapper;
import com.mofumo.api.repositories.PetRepository;
import com.mofumo.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
