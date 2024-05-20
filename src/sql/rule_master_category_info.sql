CREATE TABLE rule_master_category_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(250) UNIQUE,
    category_description VARCHAR(500),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_user VARCHAR(250),
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_user VARCHAR(250)
);

INSERT INTO rule_master_category_info (category_name, category_description, created_user, updated_user)
VALUES ('PII', 'Personally Identifiable Information', 'Admin', 'Admin'),
       ('SENSITIVE_PII', 'Sensitive Personally Identifiable Information', 'Admin', 'Admin'),
       ('NON_PII', 'Non-Personally Identifiable Information', 'Admin', 'Admin');