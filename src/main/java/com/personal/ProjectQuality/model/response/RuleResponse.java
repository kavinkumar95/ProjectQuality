package com.personal.ProjectQuality.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleResponse {

    public String ruleName;
    public String ruleDescription;
    public String createdUser;
    public String updatedUser;
    public LocalDateTime createdTime;
    public LocalDateTime updatedTime;

}
