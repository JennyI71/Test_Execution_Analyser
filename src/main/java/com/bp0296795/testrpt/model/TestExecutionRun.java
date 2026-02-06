package com.bp0296795.testrpt.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestExecutionRun {
    private String runId;
    private String suiteName;
    private String environment;
    private String triggeredBy;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<TestCaseResult> results = new ArrayList<>();

    public TestExecutionRun() {
        // Jackson requires a no-arg constructor; setters will populate fields.
    }

    public TestExecutionRun(String runId,
                            String suiteName,
                            String environment,
                            String triggeredBy,
                            LocalDateTime startTime,
                            LocalDateTime endTime,
                            List<TestCaseResult> results) {
        this.runId = runId;
        this.suiteName = suiteName;
        this.environment = environment;
        this.triggeredBy = triggeredBy;
        this.startTime = startTime;
        this.endTime = endTime;
        if (results != null) {
            this.results = new ArrayList<>(results);
        }
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<TestCaseResult> getResults() {
        return Collections.unmodifiableList(results);
    }

    public void setResults(List<TestCaseResult> results) {
        this.results = (results == null) ? new ArrayList<>() : new ArrayList<>(results);
    }
}
