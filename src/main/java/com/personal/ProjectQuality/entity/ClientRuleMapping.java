package com.personal.ProjectQuality.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_rule_mapping",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"client_id", "rule_id", "master_rule_id"})})
@Data
public class ClientRuleMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientInfo clientInfo;

    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private RuleInfo ruleInfo;

    @ManyToOne
    @JoinColumn(name = "master_rule_id", nullable = false)
    private RuleMasterCategoryInfo ruleMasterCategoryInfo;

    @Column(name = "null_check", nullable = false)
    private Boolean nullCheck;

    @Column(name = "min_value")
    private Integer minValue;

    @Column(name = "max_value")
    private Integer maxValue;

    @Column(name = "regex", length = 500)
    private String regex;

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
