package com.ncpas.laboratorio3.services;

import com.ncpas.laboratorio3.domain.dto.request.CreateSpecimenRequest;
import com.ncpas.laboratorio3.domain.dto.request.UpdateSpecimenRequest;
import com.ncpas.laboratorio3.domain.dto.response.PageableResponse;
import com.ncpas.laboratorio3.domain.dto.response.specimen.SpecimenResponse;

import java.util.UUID;

public interface SpecimenService {

    SpecimenResponse createSpecimen(CreateSpecimenRequest request);

    PageableResponse<SpecimenResponse> getAllSpecimens(
            int page,
            int size,
            String sortBy,
            String sortOrder
    );

    SpecimenResponse getSpecimenById(UUID id);

    SpecimenResponse updateSpecimen(UUID id, UpdateSpecimenRequest request);

    SpecimenResponse deleteSpecimen(UUID id);
}