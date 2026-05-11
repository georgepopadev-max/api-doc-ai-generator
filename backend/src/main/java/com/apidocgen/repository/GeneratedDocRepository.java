package com.apidocgen.repository;

import com.apidocgen.entity.GeneratedDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GeneratedDocRepository extends JpaRepository<GeneratedDoc, Long> {
    List<GeneratedDoc> findByProjectIdOrderByGeneratedAtDesc(Long projectId);
    GeneratedDoc findFirstByProjectIdOrderByVersionDesc(Long projectId);
}