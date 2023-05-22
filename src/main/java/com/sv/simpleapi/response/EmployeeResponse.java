package com.sv.simpleapi.response;

import com.sv.simpleapi.DTO.EmployeeDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record EmployeeResponse(List<EmployeeDTO> employees, int pageNo, int pageSize, long totalElements, int totalPages, boolean last) {
}
