package ru.example.grpcsample.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.example.grpcsample.dto.PetRequestDTO;
import ru.example.grpcsample.dto.PetResponseDTO;

import java.util.Map;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final Map<String, PetRequestDTO> repository;

    @Override
    public String createPet(PetRequestDTO pet) {
        String petId = UUID.randomUUID().toString();
        repository.put(petId, pet);
        return petId;
    }

    @Override
    public PetResponseDTO findByIDPet(String id) {

        PetRequestDTO petRequestDTO = repository.get(id);

        if (petRequestDTO != null) {
            return PetResponseDTO
                    .builder()
                    .petName(petRequestDTO.petName())
                    .petType(petRequestDTO.petType())
                    .build();
        } else {
            return null;
        }
    }
}