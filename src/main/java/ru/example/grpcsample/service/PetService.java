package ru.example.grpcsample.service;

import ru.example.grpcsample.dto.PetRequestDTO;
import ru.example.grpcsample.dto.PetResponseDTO;

public interface PetService {

    String createPet(PetRequestDTO pet);

    PetResponseDTO findByIDPet(String id);
}