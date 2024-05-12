package com.personal.ProjectQuality.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRuleMappingRequest {

    public String tenantCode;
    public String ruleName;
    public String ruleMasterName;
    public Boolean nullCheck;
    public Integer minValue;
    public Integer maxValue;
    public String regex;

}
