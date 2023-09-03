package com.saptarshi.das.demoservice.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataRequest {
    private String name;
    private String email;
}
