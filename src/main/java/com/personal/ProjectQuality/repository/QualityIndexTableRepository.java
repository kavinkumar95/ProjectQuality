package com.personal.ProjectQuality.repository;

import com.personal.ProjectQuality.entity.QualityIndexTableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QualityIndexTableRepository extends JpaRepository<QualityIndexTableInfo, Long> {

    @Query(value = "SELECT * FROM quality_index_table_info " +
            "WHERE table_name = :tableName " +
            "AND datasource = :datasource " +
            "AND schema_name = :schemaName " +
            "ORDER BY created_time DESC LIMIT 1",
            nativeQuery = true)
    QualityIndexTableInfo findByTableName(String datasource, String schemaName, String tableName);
}