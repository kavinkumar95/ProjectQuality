package com.personal.ProjectQuality.model;

public class ExceptionInfo {
    private String exception_message;
    private String exception_traceback;
    private boolean raised_exception;

    public String getException_message() {
        return exception_message;
    }

    public void setException_message(String exception_message) {
        this.exception_message = exception_message;
    }

    public String getException_traceback() {
        return exception_traceback;
    }

    public void setException_traceback(String exception_traceback) {
        this.exception_traceback = exception_traceback;
    }

    public boolean isRaised_exception() {
        return raised_exception;
    }

    public void setRaised_exception(boolean raised_exception) {
        this.raised_exception = raised_exception;
    }

    // getters and setters
}
