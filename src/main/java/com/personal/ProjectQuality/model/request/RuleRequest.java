package com.personal.ProjectQuality.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleRequest {

    public String ruleName;
    public String ruleDescription;

}
