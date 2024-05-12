package com.personal.ProjectQuality.repository;

import com.personal.ProjectQuality.entity.ClientInfo;
import com.personal.ProjectQuality.entity.RuleMasterCategoryInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuleMasterCategoryInfoRepository extends JpaRepository<RuleMasterCategoryInfo, Long> {


    public Optional<RuleMasterCategoryInfo> findByCategoryName(String categoryName);
}
