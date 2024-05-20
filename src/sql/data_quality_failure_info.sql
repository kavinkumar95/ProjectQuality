CREATE TABLE quality_index_failure_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id VARCHAR(255),
    schema_name VARCHAR(255),
    table_name VARCHAR(255),
    column_name VARCHAR(255),
    failed_data TEXT,
    failed_count INT,
    created_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_user VARCHAR(255) NOT NULL DEFAULT 'System',
    updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_user VARCHAR(255) NOT NULL DEFAULT 'System'
);