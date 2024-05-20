CREATE TABLE rule_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rule_name VARCHAR(250) UNIQUE,
    rule_description VARCHAR(500),
     created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_user VARCHAR(250),
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_user VARCHAR(250)
);

INSERT INTO rule_info (rule_name, rule_description,  created_user,  updated_user)
VALUES ('LATITUDE', 'LATITUDE', 'Admin', 'Admin');