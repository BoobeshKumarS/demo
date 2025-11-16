package com.hcltech.applicationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "UNIVERSITYSERVICE",
        path = "/api/universities"
)
public interface UniversityClient {

    @GetMapping("/{universityId}")
    UniversityResponseDTO getById(@PathVariable("universityId") UUID universityId);
}