CREATE TABLE client_rule_mapping (
    id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    rule_id BIGINT,
    master_rule_id INT,
    null_check BOOLEAN NOT NULL DEFAULT TRUE,
    min_value INT,
    max_value INT,
    regex VARCHAR(500),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_user VARCHAR(250),
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_user VARCHAR(250),
    FOREIGN KEY (client_id) REFERENCES client_info(id),
    FOREIGN KEY (rule_id) REFERENCES rule_info(rule_id),
    FOREIGN KEY (master_rule_id) REFERENCES rule_master_category_info(id)
);

ALTER TABLE client_rule_mapping
ADD CONSTRAINT unique_client_rule_mapping UNIQUE(client_id, rule_id, master_rule_id);