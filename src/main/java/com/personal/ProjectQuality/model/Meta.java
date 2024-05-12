package com.personal.ProjectQuality.model;

public class Meta {
    private ActiveBatchDefinition active_batch_definition;
    private BatchMarkers batch_markers;
    private BatchSpec batch_spec;
    private String checkpoint_id;
    private String checkpoint_name;
    private String expectation_suite_name;
    private String great_expectations_version;
    private RunId run_id;
    private String validation_id;
    private String validation_time;

    public ActiveBatchDefinition getActive_batch_definition() {
        return active_batch_definition;
    }

    public void setActive_batch_definition(ActiveBatchDefinition active_batch_definition) {
        this.active_batch_definition = active_batch_definition;
    }

    public BatchMarkers getBatch_markers() {
        return batch_markers;
    }

    public void setBatch_markers(BatchMarkers batch_markers) {
        this.batch_markers = batch_markers;
    }

    public BatchSpec getBatch_spec() {
        return batch_spec;
    }

    public void setBatch_spec(BatchSpec batch_spec) {
        this.batch_spec = batch_spec;
    }

    public String getCheckpoint_id() {
        return checkpoint_id;
    }

    public void setCheckpoint_id(String checkpoint_id) {
        this.checkpoint_id = checkpoint_id;
    }

    public String getCheckpoint_name() {
        return checkpoint_name;
    }

    public void setCheckpoint_name(String checkpoint_name) {
        this.checkpoint_name = checkpoint_name;
    }

    public String getExpectation_suite_name() {
        return expectation_suite_name;
    }

    public void setExpectation_suite_name(String expectation_suite_name) {
        this.expectation_suite_name = expectation_suite_name;
    }

    public String getGreat_expectations_version() {
        return great_expectations_version;
    }

    public void setGreat_expectations_version(String great_expectations_version) {
        this.great_expectations_version = great_expectations_version;
    }

    public RunId getRun_id() {
        return run_id;
    }

    public void setRun_id(RunId run_id) {
        this.run_id = run_id;
    }

    public String getValidation_id() {
        return validation_id;
    }

    public void setValidation_id(String validation_id) {
        this.validation_id = validation_id;
    }

    public String getValidation_time() {
        return validation_time;
    }

    public void setValidation_time(String validation_time) {
        this.validation_time = validation_time;
    }

    // getters and setters
}
