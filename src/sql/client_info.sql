CREATE TABLE client_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tenant_name VARCHAR(250) UNIQUE,
    tenant_description VARCHAR(500),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_user VARCHAR(250),
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_user VARCHAR(250)
);

INSERT INTO client_info (tenant_name, tenant_description, created_user, updated_user)
VALUES ('DEF', 'DEFAULT', 'def', 'def');