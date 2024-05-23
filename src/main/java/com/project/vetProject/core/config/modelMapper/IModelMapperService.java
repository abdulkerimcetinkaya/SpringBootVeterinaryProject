package com.project.vetProject.core.config.modelMapper;

import org.modelmapper.ModelMapper;

public interface IModelMapperService {

    // İstekler için ModelMapper nesnesi döndürür
    ModelMapper forRequest();

    // Yanıtlar için ModelMapper nesnesi döndürür
    ModelMapper forResponse();
}
