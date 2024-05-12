package com.personal.ProjectQuality.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
public class ValidationResult {
    private boolean success;
    private List<ResultItem> results;
    private Map<String, Object> evaluation_parameters;
    private Statistics statistics;
    private Map<String, Object> meta;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ResultItem> getResults() {
        return results;
    }

    public void setResults(List<ResultItem> results) {
        this.results = results;
    }

    public Map<String, Object> getEvaluation_parameters() {
        return evaluation_parameters;
    }

    public void setEvaluation_parameters(Map<String, Object> evaluation_parameters) {
        this.evaluation_parameters = evaluation_parameters;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    // getters and setters
}

