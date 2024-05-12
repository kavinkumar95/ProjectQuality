package com.personal.ProjectQuality.repository;

import com.personal.ProjectQuality.entity.ClientInfo;
import com.personal.ProjectQuality.entity.ClientRuleMapping;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRuleMappingRepository extends JpaRepository<ClientRuleMapping, Long> {
    public List<ClientRuleMapping> findByClientInfo(ClientInfo clientInfo);
}
