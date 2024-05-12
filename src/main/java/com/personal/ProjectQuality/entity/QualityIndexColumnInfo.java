package com.personal.ProjectQuality.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "quality_index_column_info")
@Data
public class QualityIndexColumnInfo {

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

    @Column(name = "validation_status")
    private Boolean validationStatus;

    @Column(name = "expectation_type")
    private String expectationType;

    @Column(name = "total_row_count")
    private Integer totalRowCount;

    @Column(name = "successfull_row_count")
    private Integer successfulRowCount;

    @Column(name = "failed_row_count")
    private Integer failedRowCount;

    @Column(name = "validation_result")
    private Float validationResult;

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
