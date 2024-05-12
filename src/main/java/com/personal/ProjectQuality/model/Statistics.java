package com.personal.ProjectQuality.model;

public class Statistics {
    private int evaluated_expectations;
    private double success_percent;
    private int successful_expectations;
    private int unsuccessful_expectations;

    public int getEvaluated_expectations() {
        return evaluated_expectations;
    }

    public void setEvaluated_expectations(int evaluated_expectations) {
        this.evaluated_expectations = evaluated_expectations;
    }

    public double getSuccess_percent() {
        return success_percent;
    }

    public void setSuccess_percent(double success_percent) {
        this.success_percent = success_percent;
    }

    public int getSuccessful_expectations() {
        return successful_expectations;
    }

    public void setSuccessful_expectations(int successful_expectations) {
        this.successful_expectations = successful_expectations;
    }

    public int getUnsuccessful_expectations() {
        return unsuccessful_expectations;
    }

    public void setUnsuccessful_expectations(int unsuccessful_expectations) {
        this.unsuccessful_expectations = unsuccessful_expectations;
    }

    // getters and setters
}
