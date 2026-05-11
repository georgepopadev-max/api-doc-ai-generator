package com.apidocgen.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "generated_docs")
public class GeneratedDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private DocProject project;

    private String title;

    private String docType; // OPENAPI, ASYNCAPI, MARKDOWN, HTML

    @Column(columnDefinition = "TEXT")
    private String content;

    private String format; // YAML, JSON, MARKDOWN, HTML

    private LocalDateTime generatedAt;

    private int version;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
        if (version == 0) version = 1;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DocProject getProject() { return project; }
    public void setProject(DocProject project) { this.project = project; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDocType() { return docType; }
    public void setDocType(String docType) { this.docType = docType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
}