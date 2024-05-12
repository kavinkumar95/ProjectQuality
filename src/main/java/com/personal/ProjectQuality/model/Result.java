package com.personal.ProjectQuality.model;

import java.util.List;

public class Result {
    private int element_count;
    private double missing_count;
    private double missing_percent;
    private List<PartialUnexpectedCount> partial_unexpected_counts;
    private List<String> partial_unexpected_list;
    private double unexpected_count;
    private double unexpected_percent;
    private double unexpected_percent_nonmissing;
    private double unexpected_percent_total;

    public int getElement_count() {
        return element_count;
    }

    public void setElement_count(int element_count) {
        this.element_count = element_count;
    }

    public double getMissing_count() {
        return missing_count;
    }

    public void setMissing_count(double missing_count) {
        this.missing_count = missing_count;
    }

    public double getMissing_percent() {
        return missing_percent;
    }

    public void setMissing_percent(double missing_percent) {
        this.missing_percent = missing_percent;
    }

    public List<PartialUnexpectedCount> getPartial_unexpected_counts() {
        return partial_unexpected_counts;
    }

    public void setPartial_unexpected_counts(List<PartialUnexpectedCount> partial_unexpected_counts) {
        this.partial_unexpected_counts = partial_unexpected_counts;
    }

    public List<String> getPartial_unexpected_list() {
        return partial_unexpected_list;
    }

    public void setPartial_unexpected_list(List<String> partial_unexpected_list) {
        this.partial_unexpected_list = partial_unexpected_list;
    }

    public double getUnexpected_count() {
        return unexpected_count;
    }

    public void setUnexpected_count(double unexpected_count) {
        this.unexpected_count = unexpected_count;
    }

    public double getUnexpected_percent() {
        return unexpected_percent;
    }

    public void setUnexpected_percent(double unexpected_percent) {
        this.unexpected_percent = unexpected_percent;
    }

    public double getUnexpected_percent_nonmissing() {
        return unexpected_percent_nonmissing;
    }

    public void setUnexpected_percent_nonmissing(double unexpected_percent_nonmissing) {
        this.unexpected_percent_nonmissing = unexpected_percent_nonmissing;
    }

    public double getUnexpected_percent_total() {
        return unexpected_percent_total;
    }

    public void setUnexpected_percent_total(double unexpected_percent_total) {
        this.unexpected_percent_total = unexpected_percent_total;
    }

    // getters and setters
}
