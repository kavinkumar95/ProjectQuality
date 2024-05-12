package com.personal.ProjectQuality.model;

import java.util.Map;

public class ExpectationConfig {
    private String expectation_type;
    private Map<String, Object> kwargs;
    private Map<String, Object> meta;

    public String getExpectation_type() {
        return expectation_type;
    }

    public void setExpectation_type(String expectation_type) {
        this.expectation_type = expectation_type;
    }

    public Map<String, Object> getKwargs() {
        return kwargs;
    }

    public void setKwargs(Map<String, Object> kwargs) {
        this.kwargs = kwargs;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    // getters and setters
}
