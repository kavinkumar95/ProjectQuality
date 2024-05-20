CREATE TABLE quality_index_column_info (
    id BIGINT NOT NULL AUTO_INCREMENT,
    job_id VARCHAR(255),
	schema_name VARCHAR(500),
    table_name VARCHAR(255),
	column_name VARCHAR(255),
    validation_status BOOLEAN,
    expectation_type VARCHAR(500),
    total_row_count INT,
    successfull_row_count INT,
    failed_row_count INT,
    validation_result FLOAT,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_user VARCHAR(255) DEFAULT 'System',
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	updated_user VARCHAR(255) DEFAULT 'System',
    PRIMARY KEY (id)
);