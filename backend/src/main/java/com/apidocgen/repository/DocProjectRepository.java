package com.apidocgen.repository;

import com.apidocgen.entity.DocProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocProjectRepository extends JpaRepository<DocProject, Long> {
}