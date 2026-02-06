package com.bp0296795.testrpt.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestCaseResult {
    private String testId;
    private String name;
    private TestStatus status;
    private long durationMillis;
    private FailureType failureType;
    private String failureMessage;
    private Set<String> tags = new HashSet<>();

    public TestCaseResult() {
        // Jackson requires a no-arg constructor; setters will populate fields.
    }

    public TestCaseResult(String testId,
                          String name,
                          TestStatus status,
                          long durationMillis,
                          FailureType failureType,
                          String failureMessage,
                          Set<String> tags) {
        this.testId = testId;
        this.name = name;
        this.status = status;
        this.durationMillis = durationMillis;
        this.failureType = failureType;
        this.failureMessage = failureMessage;
        if (tags != null) {
            this.tags = new HashSet<>(tags);
        }
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public FailureType getFailureType() {
        return failureType;
    }

    public void setFailureType(FailureType failureType) {
        this.failureType = failureType;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public void setTags(Set<String> tags) {
        this.tags = (tags == null) ? new HashSet<>() : new HashSet<>(tags);
    }
}
