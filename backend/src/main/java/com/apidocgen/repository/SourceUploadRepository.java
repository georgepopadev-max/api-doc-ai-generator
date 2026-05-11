package com.apidocgen.repository;

import com.apidocgen.entity.SourceUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SourceUploadRepository extends JpaRepository<SourceUpload, Long> {
    List<SourceUpload> findByProjectId(Long projectId);
}