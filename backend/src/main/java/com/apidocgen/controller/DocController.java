package com.apidocgen.controller;

import com.apidocgen.entity.DocProject;
import com.apidocgen.entity.GeneratedDoc;
import com.apidocgen.repository.DocProjectRepository;
import com.apidocgen.repository.GeneratedDocRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DocController {

    private final GeneratedDocRepository docRepository;
    private final DocProjectRepository projectRepository;

    public DocController(GeneratedDocRepository docRepository, DocProjectRepository projectRepository) {
        this.docRepository = docRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projects/{projectId}/docs")
    public ResponseEntity<List<GeneratedDoc>> getProjectDocs(@PathVariable Long projectId) {
        return ResponseEntity.ok(docRepository.findByProjectIdOrderByGeneratedAtDesc(projectId));
    }

    @GetMapping("/projects/{projectId}/docs/latest")
    public ResponseEntity<GeneratedDoc> getLatestDoc(@PathVariable Long projectId) {
        GeneratedDoc doc = docRepository.findFirstByProjectIdOrderByVersionDesc(projectId);
        if (doc == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doc);
    }

    @PostMapping("/projects/{projectId}/docs")
    public ResponseEntity<GeneratedDoc> generateDoc(@PathVariable Long projectId, @RequestBody Map<String, String> body) {
        return projectRepository.findById(projectId).map(project -> {
            GeneratedDoc doc = new GeneratedDoc();
            doc.setProject(project);
            doc.setTitle(body.getOrDefault("title", "Generated Documentation"));
            doc.setDocType(body.getOrDefault("docType", "OPENAPI"));
            doc.setFormat(body.getOrDefault("format", "YAML"));
            doc.setContent(generateSampleContent(project, body));
            doc.setVersion(docRepository.findFirstByProjectIdOrderByVersionDesc(projectId).getVersion() + 1);
            return ResponseEntity.ok(docRepository.save(doc));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/docs/{docId}")
    public ResponseEntity<GeneratedDoc> getDoc(@PathVariable Long docId) {
        return docRepository.findById(docId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/docs/{docId}")
    public ResponseEntity<GeneratedDoc> updateDoc(@PathVariable Long docId, @RequestBody String yamlContent) {
        return docRepository.findById(docId).map(doc -> {
            doc.setContent(yamlContent);
            return ResponseEntity.ok(docRepository.save(doc));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/docs/{docId}/yaml")
    public ResponseEntity<String> exportYaml(@PathVariable Long docId) {
        return docRepository.findById(docId)
                .map(doc -> ResponseEntity.ok(doc.getContent()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/docs/{docId}/json")
    public ResponseEntity<String> exportJson(@PathVariable Long docId) {
        return docRepository.findById(docId)
                .map(doc -> {
                    String json = doc.getContent()
                            .replace("openapi:", "  \"openapi\":")
                            .replace("info:", "  \"info\":")
                            .replace("paths:", "  \"paths\":");
                    return ResponseEntity.ok(json);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateYaml(@RequestBody String yaml) {
        boolean valid = yaml != null && yaml.contains("openapi");
        Map<String, Object> result = Map.of(
            "valid", valid,
            "message", valid ? "YAML is valid" : "Invalid YAML structure"
        );
        return ResponseEntity.ok(result);
    }

    private String generateSampleContent(DocProject project, Map<String, String> body) {
        String docType = body.getOrDefault("docType", "OPENAPI");
        if ("OPENAPI".equals(docType)) {
            return """
                openapi: 3.0.3
                info:
                  title: %s API
                  description: Auto-generated API documentation
                  version: 1.0.0
                servers:
                  - url: https://api.example.com
                    description: Production server
                paths:
                  /health:
                    get:
                      summary: Health check endpoint
                      responses:
                        '200':
                          description: Service is healthy
                """.formatted(project.getName());
        }
        return "# Documentation for " + project.getName();
    }
}