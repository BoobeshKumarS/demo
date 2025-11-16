package com.hcltech.applicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import com.hcltech.applicationservice.entity.DocumentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentRequestDTO {
    private DocumentType type;
    private MultipartFile file;
    private String filename;
}