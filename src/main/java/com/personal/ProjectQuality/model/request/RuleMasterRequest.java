package com.personal.ProjectQuality.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleMasterRequest {

    public String ruleMasterName;
    public String ruleMasterDescription;

}
