package com.personal.ProjectQuality.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRuleMappingResponse {

    public String tenantCode;
    public String ruleName;
    public String ruleMasterName;
    public Boolean nullCheck;
    public Integer minValue;
    public Integer maxValue;
    public String regex;
    public String createdUser;
    public String updatedUser;
    public LocalDateTime createdTime;
    public LocalDateTime updatedTime;

}
