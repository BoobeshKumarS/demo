package com.hcltech.applicationservice.entity;

import jakarta.persistence.*;
import lombok.*;

//@Entity
@Table(name = "application_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private DocumentType tyspe;

    private String filename;

    private long size;
    private String mimeType;    // File content type (e.g., image/png, application/pdf)

    @ManyToOne
    @JoinColumn(name = "application_form_id")
    private ApplicationForm applicationForm;
}