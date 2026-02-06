package com.example.testrpt.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnalysisResult {
    private final int totalRuns;
    private final int totalTests;
    private final Map<TestStatus, Integer> statusCounts;
    private final Map<FailureType, Integer> failureTypeCounts;
    private final Map<String, Integer> failuresByTestId;
    private final Map<String, Double> failureRateByTestId;

    public AnalysisResult(int totalRuns,
                          int totalTests,
                          Map<TestStatus, Integer> statusCounts,
                          Map<FailureType, Integer> failureTypeCounts,
                          Map<String, Integer> failuresByTestId,
                          Map<String, Double> failureRateByTestId) {
        this.totalRuns = totalRuns;
        this.totalTests = totalTests;
        this.statusCounts = Collections.unmodifiableMap(new HashMap<>(statusCounts));
        this.failureTypeCounts = Collections.unmodifiableMap(new HashMap<>(failureTypeCounts));
        this.failuresByTestId = Collections.unmodifiableMap(new HashMap<>(failuresByTestId));
        this.failureRateByTestId = Collections.unmodifiableMap(new HashMap<>(failureRateByTestId));
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public Map<TestStatus, Integer> getStatusCounts() {
        return statusCounts;
    }

    public Map<FailureType, Integer> getFailureTypeCounts() {
        return failureTypeCounts;
    }

    public Map<String, Integer> getFailuresByTestId() {
        return failuresByTestId;
    }

    public Map<String, Double> getFailureRateByTestId() {
        return failureRateByTestId;
    }
}
