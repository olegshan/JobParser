package com.olegshan.repository;

import com.olegshan.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, String> {

	Page<Job> findAllByCompanyIgnoreCase(String company, Pageable request);
}
