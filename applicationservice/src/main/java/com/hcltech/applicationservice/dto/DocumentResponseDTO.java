package com.hcltech.applicationservice.dto;

import com.hcltech.applicationservice.entity.DocumentType;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponseDTO {
    private Long id;
    private DocumentType type;
    private String filename;
    private String filePath;
    private long size;
    private String mimeType;
}
