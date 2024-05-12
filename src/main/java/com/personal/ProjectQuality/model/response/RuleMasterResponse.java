package com.personal.ProjectQuality.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleMasterResponse {

    public String ruleMasterName;
    public String ruleMasterDescription;
    public String createdUser;
    public String updatedUser;
    public LocalDateTime createdTime;
    public LocalDateTime updatedTime;

}
