package com.mofumo.api.services;

import com.mofumo.api.controllers.RegisterProviderRequest;
import com.mofumo.api.dtos.ProviderDto;
import com.mofumo.api.mappers.ProviderMapper;
import com.mofumo.api.repositories.ProviderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProviderService {
  private final ProviderMapper providerMapper;
  private final ProviderRepository providerRepository;

  public ProviderDto registerProvider(RegisterProviderRequest request) {
    var provider = providerMapper.toEntity(request);
    providerRepository.save(provider);
    return providerMapper.toDto(provider);
  }
}
