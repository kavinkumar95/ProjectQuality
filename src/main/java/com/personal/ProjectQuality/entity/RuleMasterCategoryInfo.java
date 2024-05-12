package com.personal.ProjectQuality.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rule_master_category_info")
@Data
public class RuleMasterCategoryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", unique = true, length = 250)
    private String categoryName;

    @Column(name = "category_description", length = 500)
    private String categoryDescription;

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
