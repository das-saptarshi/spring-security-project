package com.saptarshi.das.admin.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExceptionResponse {
    private List<String> errors;
}
