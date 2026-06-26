package com.ncpas.laboratorio3.controllers;

import com.ncpas.laboratorio3.domain.dto.request.CreateSpecimenRequest;
import com.ncpas.laboratorio3.domain.dto.request.UpdateSpecimenRequest;
import com.ncpas.laboratorio3.domain.dto.response.GeneralResponse;
import com.ncpas.laboratorio3.services.SpecimenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/specimens")
public class SpecimenController {

    private final SpecimenService specimenService;

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse> createSpecimen(
            @RequestBody @Valid CreateSpecimenRequest request
    ) {
        return buildResponse(
                "Specimen created successfully",
                HttpStatus.CREATED,
                specimenService.createSpecimen(request)
        );
    }

    @GetMapping("/getBy/{id}")
    public ResponseEntity<GeneralResponse> getSpecimenById(
            @PathVariable UUID id
    ) {
        return buildResponse(
                "Specimen found",
                HttpStatus.OK,
                specimenService.getSpecimenById(id)
        );
    }

    @GetMapping("/getAll")
    public ResponseEntity<GeneralResponse> getAllSpecimens(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        return buildResponse(
                "Specimens found",
                HttpStatus.OK,
                specimenService.getAllSpecimens(page, size, sortBy, sortOrder)
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GeneralResponse> updateSpecimen(
            @PathVariable UUID id,
            @RequestBody UpdateSpecimenRequest request
    ) {
        return buildResponse(
                "Specimen updated successfully",
                HttpStatus.OK,
                specimenService.updateSpecimen(id, request)
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GeneralResponse> deleteSpecimen(
            @PathVariable UUID id
    ) {
        return buildResponse(
                "Specimen deleted successfully",
                HttpStatus.OK,
                specimenService.deleteSpecimen(id)
        );
    }

    private ResponseEntity<GeneralResponse> buildResponse(
            String message,
            HttpStatus status,
            Object data
    ) {
        String uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .build()
                .getPath();

        return ResponseEntity
                .status(status)
                .body(GeneralResponse.builder()
                        .uri(uri)
                        .message(message)
                        .status(status.value())
                        .time(LocalDateTime.now())
                        .data(data)
                        .build());
    }
}