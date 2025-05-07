package ru.example.grpcsample;

import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.example.grpc.server.pet.PetOuterClass;
import ru.example.grpc.server.pet.PetServiceGrpc;
import ru.example.grpcsample.dto.PetRequestDTO;
import ru.example.grpcsample.dto.PetResponseDTO;
import ru.example.grpcsample.service.PetService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Log4j2
@SpringJUnitConfig(classes = {GRpcInSpringBootSampleApplication.class})
@SpringBootTest(properties = {
        "grpc.server.inProcessName=test",
        "grpc.server.port=9091",
        "grpc.client.petService.address=in-process:test"
})
class GRpcInSpringBootSampleApplicationTests {

    @MockitoBean
    PetService petService;
    @GrpcClient("petService")
    private PetServiceGrpc.PetServiceBlockingStub petServiceBlockingStub;
    String petId = "1";
    PetOuterClass.FindByIdPetResponse.Pet pet;
    PetRequestDTO petRequestDTO;
    PetResponseDTO petResponseDTO;

    @BeforeEach
    void setUp() {
        pet = PetOuterClass.FindByIdPetResponse.Pet
                .newBuilder()
                .setPetName("Bob")
                .setPetType("dog")
                .build();

        petRequestDTO = PetRequestDTO
                .builder()
                .petName("Bob")
                .petType("dog")
                .petBirthDate("2020-12-11")
                .build();

        petResponseDTO = PetResponseDTO
                .builder()
                .petName("Bob")
                .petType("dog")
                .build();
    }

    @Test
    @DisplayName("JUnit grpc test for create pet")
    void createPetTest() {

        when(petService.createPet(any())).thenReturn(petId);

        PetOuterClass.CreatePetRequest request = PetOuterClass.CreatePetRequest
                .newBuilder()
                .setPet(PetOuterClass.CreatePetRequest.Pet
                        .newBuilder()
                        .setPetName("Bob")
                        .setPetType("dog")
                        .setPetBirthDate("2020-12-11")
                        .build())
                .build();

        PetOuterClass.CreatePetResponse createPetResponse =
                petServiceBlockingStub.createPet(request);

        assertThat(createPetResponse).isNotNull();
        assertThat(petId).isEqualTo(createPetResponse.getPetId());

        verify(petService).createPet(petRequestDTO);
    }

    @Test
    @DisplayName("JUnit grpc test for find pet by id")
    void findByIDPetTest() {

        when(petService.findByIDPet(anyString())).thenReturn(petResponseDTO);

        PetOuterClass.FindByIdPetRequest request =
                PetOuterClass.FindByIdPetRequest
                        .newBuilder()
                        .setPetId(petId)
                        .build();

        PetOuterClass.FindByIdPetResponse response =
                petServiceBlockingStub.findByIDPet(request);

        assertThat(response).isNotNull();
        assertThat(response.getPet().getPetName()).isEqualTo(pet.getPetName());
        verify(petService).findByIDPet(petId);
    }
}