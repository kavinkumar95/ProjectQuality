package com.personal.ProjectQuality.repository;

import com.personal.ProjectQuality.entity.QualityIndexColumnInfo;
import com.personal.ProjectQuality.entity.QualityIndexTableInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualityIndexColumnRepository extends JpaRepository<QualityIndexColumnInfo, Long> {


    List<QualityIndexColumnInfo> findByJobId(String jobId);
}