package com.personal.ProjectQuality.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_info")
@Data
public class ClientInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_name", unique = true, length = 250)
    private String tenantName;

    @Column(name = "tenant_description", length = 500)
    private String tenantDescription;

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
