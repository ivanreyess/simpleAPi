package com.sv.simpleapi.DTO;

import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record EmployeeDTO(Long id, String name, String email, String lastName, LocalDateTime createdAt) {
}
