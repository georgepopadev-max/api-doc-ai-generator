package com.apidocgen.config;

import com.apidocgen.entity.DocProject;
import com.apidocgen.entity.GeneratedDoc;
import com.apidocgen.entity.SourceUpload;
import com.apidocgen.repository.DocProjectRepository;
import com.apidocgen.repository.GeneratedDocRepository;
import com.apidocgen.repository.SourceUploadRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(DocProjectRepository projectRepository,
                               GeneratedDocRepository docRepository,
                               SourceUploadRepository uploadRepository) {
        return args -> {
            if (projectRepository.count() == 0) {
                DocProject p1 = new DocProject();
                p1.setName("User Management API");
                p1.setDescription("REST API for user registration, authentication and profile management");
                p1.setFramework("SPRING");
                p1.setStatus("COMPLETED");
                p1 = projectRepository.save(p1);

                GeneratedDoc d1 = new GeneratedDoc();
                d1.setProject(p1);
                d1.setTitle("User API v1.0");
                d1.setDocType("OPENAPI");
                d1.setFormat("YAML");
                d1.setContent("openapi: 3.0.3\ninfo:\n  title: User Management API\n  version: 1.0.0\npaths:\n  /users:\n    get:\n      summary: List all users\n      responses:\n        '200':\n          description: List of users");
                d1.setVersion(1);
                docRepository.save(d1);

                SourceUpload u1 = new SourceUpload();
                u1.setProject(p1);
                u1.setFilename("UserController.java");
                u1.setFileSize(4521);
                u1.setLanguage("JAVA");
                u1.setSourceCode("@RestController\n@RequestMapping(\"/api/users\")\npublic class UserController { ... }");
                uploadRepository.save(u1);

                DocProject p2 = new DocProject();
                p2.setName("Order Service API");
                p2.setDescription("E-commerce order processing and fulfillment API");
                p2.setFramework("SPRING");
                p2.setStatus("PROCESSING");
                projectRepository.save(p2);

                DocProject p3 = new DocProject();
                p3.setName("Notification Hub");
                p3.setDescription("Multi-channel notification delivery system");
                p3.setFramework("EXPRESS");
                p3.setStatus("DRAFT");
                projectRepository.save(p3);
            }
        };
    }
}