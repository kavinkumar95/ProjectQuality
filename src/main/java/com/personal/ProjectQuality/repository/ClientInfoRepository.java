package com.personal.ProjectQuality.repository;

import com.personal.ProjectQuality.entity.ClientInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientInfoRepository extends JpaRepository<ClientInfo, Long> {
    public Optional<ClientInfo> findByTenantName(String tenantName);


}
