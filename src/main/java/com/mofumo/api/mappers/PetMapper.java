package com.mofumo.api.mappers;

import com.mofumo.api.dtos.AddNewPetRequest;
import com.mofumo.api.dtos.PetDto;
import com.mofumo.api.entities.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetMapper {
  Pet toEntity(AddNewPetRequest request);
  PetDto toDto(Pet pet);
}
