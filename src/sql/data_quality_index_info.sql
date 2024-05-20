CREATE TABLE quality_index_table_info (
    id BIGINT NOT NULL AUTO_INCREMENT,
    job_id VARCHAR(255) UNIQUE,
    datasource VARCHAR(50),
	schema_name VARCHAR(500),
    table_name VARCHAR(255),
    job_status BOOLEAN,
    total_validation_count INT,
    successfull_validation_count INT,
    failed_validation_count INT,
    validation_result FLOAT,
    validation_comment VARCHAR(500),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_user VARCHAR(255) DEFAULT 'System',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	updated_user VARCHAR(255) DEFAULT 'System',
    PRIMARY KEY (id)
);