package com.personal.ProjectQuality.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantRequest {

    public String clinetCode;
    public String clinetDescription;

}
