package com.mofumo.api.mappers;

import com.mofumo.api.controllers.RegisterProviderRequest;
import com.mofumo.api.dtos.ProviderDto;
import com.mofumo.api.entities.Provider;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProviderMapper {
  Provider toEntity(RegisterProviderRequest request);
  ProviderDto toDto(Provider provider);

}
