package com.personal.ProjectQuality.model;

import java.util.Map;

public class ResultItem {
    private boolean success;
    private ExpectationConfig expectation_config;
    private Result result;
    private Map<String, Object> meta;
    private ExceptionInfo exception_info;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ExpectationConfig getExpectation_config() {
        return expectation_config;
    }

    public void setExpectation_config(ExpectationConfig expectation_config) {
        this.expectation_config = expectation_config;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public ExceptionInfo getException_info() {
        return exception_info;
    }

    public void setException_info(ExceptionInfo exception_info) {
        this.exception_info = exception_info;
    }

    // getters and setters
}
