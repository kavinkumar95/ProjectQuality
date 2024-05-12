package com.personal.ProjectQuality.repository;

import com.personal.ProjectQuality.entity.RuleInfo;
import com.personal.ProjectQuality.entity.RuleMasterCategoryInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuleInfoRepository extends JpaRepository<RuleInfo, Long> {
    public Optional<RuleInfo> findByRuleName(String ruleName);
}
