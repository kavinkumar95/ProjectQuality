package com.personal.ProjectQuality.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rule_info")
@Data
public class RuleInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long id;

    @Column(name = "rule_name", unique = true, length = 250)
    private String ruleName;

    @Column(name = "rule_description", length = 500)
    private String ruleDescription;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "created_user", length = 250)
    private String createdUser;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @Column(name = "updated_user", length = 250)
    private String updatedUser;

    // Getters and setters
}
