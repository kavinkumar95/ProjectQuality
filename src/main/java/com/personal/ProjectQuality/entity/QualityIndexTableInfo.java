package com.personal.ProjectQuality.entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quality_index_table_info")
@Data
public class QualityIndexTableInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", unique = true)
    private String jobId;

    @Column(name = "datasource")
    private String datasource;

    @Column(name = "schema_name")
    private String schemaName;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "job_status")
    private Boolean status;

    @Column(name = "total_validation_count")
    private Integer totalValidationCount;

    @Column(name = "successfull_validation_count")
    private Integer successfullValidationCount;

    @Column(name = "failed_validation_count")
    private Integer failedValidationCount;

    @Column(name = "validation_result")
    private Float validationResult;

    @Column(name = "validation_comment", length = 500)
    private String validationComment;

    @Column(name = "created_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdTime;

    @Column(name = "created_user", columnDefinition = "VARCHAR(255) DEFAULT 'system'")
    private String createdUser;

    @Column(name = "updated_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime updatedTime;

    @Column(name = "updated_user", columnDefinition = "VARCHAR(255) DEFAULT 'system'")
    private String updatedUser;

   }
