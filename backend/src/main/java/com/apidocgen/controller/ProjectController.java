package com.apidocgen.controller;

import com.apidocgen.entity.DocProject;
import com.apidocgen.entity.SourceUpload;
import com.apidocgen.repository.DocProjectRepository;
import com.apidocgen.repository.SourceUploadRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final DocProjectRepository projectRepository;
    private final SourceUploadRepository uploadRepository;

    public ProjectController(DocProjectRepository projectRepository, SourceUploadRepository uploadRepository) {
        this.projectRepository = projectRepository;
        this.uploadRepository = uploadRepository;
    }

    @GetMapping("/projects")
    public ResponseEntity<List<DocProject>> getAllProjects() {
        return ResponseEntity.ok(projectRepository.findAll());
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<DocProject> getProject(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/projects")
    public ResponseEntity<DocProject> createProject(@RequestBody Map<String, String> body) {
        DocProject project = new DocProject();
        project.setName(body.get("name"));
        project.setDescription(body.get("description"));
        project.setFramework(body.getOrDefault("framework", "SPRING"));
        return ResponseEntity.ok(projectRepository.save(project));
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<DocProject> updateProject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return projectRepository.findById(id).map(project -> {
            project.setName(body.getOrDefault("name", project.getName()));
            project.setDescription(body.getOrDefault("description", project.getDescription()));
            project.setFramework(body.getOrDefault("framework", project.getFramework()));
            project.setStatus(body.getOrDefault("status", project.getStatus()));
            return ResponseEntity.ok(projectRepository.save(project));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/projects/{id}/upload")
    public ResponseEntity<SourceUpload> uploadSource(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return projectRepository.findById(id).map(project -> {
            SourceUpload upload = new SourceUpload();
            upload.setProject(project);
            upload.setFilename((String) body.get("filename"));
            upload.setFileSize(((Number) body.get("fileSize")).longValue());
            upload.setSourceCode((String) body.get("sourceCode"));
            upload.setLanguage((String) body.getOrDefault("language", "JAVA"));
            return ResponseEntity.ok(uploadRepository.save(upload));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/projects/{id}/uploads")
    public ResponseEntity<List<SourceUpload>> getProjectUploads(@PathVariable Long id) {
        return ResponseEntity.ok(uploadRepository.findByProjectId(id));
    }
}