package com.ncpas.laboratorio3.services.impl;

import com.ncpas.laboratorio3.domain.dto.request.CreateSpecimenRequest;
import com.ncpas.laboratorio3.domain.dto.request.UpdateSpecimenRequest;
import com.ncpas.laboratorio3.domain.dto.response.PageableResponse;
import com.ncpas.laboratorio3.domain.dto.response.specimen.SpecimenResponse;
import com.ncpas.laboratorio3.exceptions.ResourceNotFoundException;
import com.ncpas.laboratorio3.mappers.SpecimenMapper;
import com.ncpas.laboratorio3.repositories.SpecimenRepository;
import com.ncpas.laboratorio3.services.SpecimenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecimenServiceImpl implements SpecimenService {

    private final SpecimenRepository specimenRepository;
    private final SpecimenMapper specimenMapper;

    @Override
    @Transactional
    public SpecimenResponse createSpecimen(CreateSpecimenRequest request) {
        return specimenMapper.toDto(
                specimenRepository.save(specimenMapper.toEntityCreate(request))
        );
    }

    @Override
    public PageableResponse<SpecimenResponse> getAllSpecimens(
            int page,
            int size,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SpecimenResponse> specimenPage =
                specimenMapper.toDtoPage(specimenRepository.findAll(pageable));

        if (specimenPage.getTotalElements() == 0) {
            throw new ResourceNotFoundException("No specimens are registered in Hyrule");
        }

        return PageableResponse.<SpecimenResponse>builder()
                .content(specimenPage.getContent())
                .page(specimenPage.getNumber())
                .size(specimenPage.getSize())
                .totalElements(specimenPage.getTotalElements())
                .totalPages(specimenPage.getTotalPages())
                .last(specimenPage.isLast())
                .build();
    }

    @Override
    public SpecimenResponse getSpecimenById(UUID id) {
        return specimenMapper.toDto(
                specimenRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Specimen not found in Hyrule Records")
                        )
        );
    }

    @Override
    @Transactional
    public SpecimenResponse updateSpecimen(UUID id, UpdateSpecimenRequest request) {
        this.getSpecimenById(id);

        return specimenMapper.toDto(
                specimenRepository.save(specimenMapper.toEntityUpdate(request, id))
        );
    }

    @Override
    @Transactional
    public SpecimenResponse deleteSpecimen(UUID id) {
        SpecimenResponse existSpecimen = this.getSpecimenById(id);
        specimenRepository.deleteById(id);
        return existSpecimen;
    }
}