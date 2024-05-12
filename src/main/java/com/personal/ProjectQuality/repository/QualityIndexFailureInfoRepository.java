package com.personal.ProjectQuality.repository;

import com.personal.ProjectQuality.entity.QualityIndexColumnInfo;
import com.personal.ProjectQuality.entity.QualityIndexFailureInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualityIndexFailureInfoRepository extends JpaRepository<QualityIndexFailureInfo, Long> {
    List<QualityIndexFailureInfo> findByJobId(String jobId);
}