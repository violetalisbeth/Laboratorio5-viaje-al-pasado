package com.ncpas.laboratorio3.services.impl;

import com.ncpas.laboratorio3.domain.dto.request.CreateSpecimenRequest;
import com.ncpas.laboratorio3.domain.dto.response.specimen.SpecimenResponse;
import com.ncpas.laboratorio3.domain.entities.Specimen;
import com.ncpas.laboratorio3.exceptions.ResourceNotFoundException;
import com.ncpas.laboratorio3.mappers.SpecimenMapper;
import com.ncpas.laboratorio3.repositories.SpecimenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpecimenServiceImplTest {

    @Mock
    private SpecimenRepository specimenRepository;

    @Mock
    private SpecimenMapper specimenMapper;

    @InjectMocks
    private SpecimenServiceImpl specimenService;

    private UUID specimenId;
    private CreateSpecimenRequest createRequest;
    private Specimen specimenToSave;
    private Specimen savedSpecimen;
    private SpecimenResponse specimenResponse;

    @BeforeEach
    void setUp() {
        specimenId = UUID.randomUUID();

        createRequest = CreateSpecimenRequest.builder()
                .name("Guardian")
                .region("Central Hyrule")
                .dangerLevel(8)
                .isFriendly(false)
                .build();

        specimenToSave = Specimen.builder()
                .name(createRequest.getName())
                .region(createRequest.getRegion())
                .dangerLevel(createRequest.getDangerLevel())
                .isFriendly(createRequest.getIsFriendly())
                .build();

        savedSpecimen = Specimen.builder()
                .id(specimenId)
                .name(createRequest.getName())
                .region(createRequest.getRegion())
                .dangerLevel(createRequest.getDangerLevel())
                .isFriendly(createRequest.getIsFriendly())
                .build();

        specimenResponse = SpecimenResponse.builder()
                .id(specimenId)
                .name(savedSpecimen.getName())
                .region(savedSpecimen.getRegion())
                .dangerLevel(savedSpecimen.getDangerLevel())
                .isFriendly(savedSpecimen.getIsFriendly())
                .build();
    }

    @Test
    void createSpecimen_shouldSaveAndReturnSpecimen() {
        when(specimenMapper.toEntityCreate(createRequest))
                .thenReturn(specimenToSave);

        when(specimenRepository.save(specimenToSave))
                .thenReturn(savedSpecimen);

        when(specimenMapper.toDto(savedSpecimen))
                .thenReturn(specimenResponse);

        SpecimenResponse result =
                specimenService.createSpecimen(createRequest);

        assertThat(result).isEqualTo(specimenResponse);

        verify(specimenMapper).toEntityCreate(createRequest);
        verify(specimenRepository).save(specimenToSave);
        verify(specimenMapper).toDto(savedSpecimen);
    }

    @Test
    void getSpecimenById_shouldReturnSpecimen_whenSpecimenExists() {
        when(specimenRepository.findById(specimenId))
                .thenReturn(Optional.of(savedSpecimen));

        when(specimenMapper.toDto(savedSpecimen))
                .thenReturn(specimenResponse);

        SpecimenResponse result =
                specimenService.getSpecimenById(specimenId);

        assertThat(result).isEqualTo(specimenResponse);

        verify(specimenRepository).findById(specimenId);
        verify(specimenMapper).toDto(savedSpecimen);
    }

    @Test
    void getSpecimenById_shouldThrowException_whenSpecimenDoesNotExist() {
        when(specimenRepository.findById(specimenId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                specimenService.getSpecimenById(specimenId)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Specimen not found in Hyrule Records");

        verify(specimenRepository).findById(specimenId);
    }
}