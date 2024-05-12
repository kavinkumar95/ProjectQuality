package com.personal.ProjectQuality.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "quality_index_failure_info")
@Data
public class QualityIndexFailureInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "schema_name")
    private String schemaName;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "failed_data")
    private String failedData;

    @Column(name = "failed_count")
    private Integer failedCount;

    @Column(name = "created_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp createdTime;

    @Column(name = "created_user", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'System'")
    private String createdUser;

    @Column(name = "updated_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private Timestamp updatedTime;

    @Column(name = "updated_user", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'System'")
    private String updatedUser;

    // Getters and setters omitted for brevity
}
