package com.example.testrpt.service;

import com.example.testrpt.model.AnalysisResult;
import com.example.testrpt.model.FailureType;
import com.example.testrpt.model.TestCaseResult;
import com.example.testrpt.model.TestExecutionRun;
import com.example.testrpt.model.TestStatus;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestExecutionAnalyzer {
    public AnalysisResult analyze(List<TestExecutionRun> runs) {
        int totalRuns = runs.size();
        int totalTests = 0;

        Map<TestStatus, Integer> statusCounts = new EnumMap<>(TestStatus.class);
        Map<FailureType, Integer> failureTypeCounts = new EnumMap<>(FailureType.class);
        Map<String, Integer> failuresByTestId = new HashMap<>();
        Map<String, Integer> executionsByTestId = new HashMap<>();

        for (TestExecutionRun run : runs) {
            for (TestCaseResult tc : run.getResults()) {
                totalTests++;
                statusCounts.merge(tc.getStatus(), 1, Integer::sum);
                executionsByTestId.merge(tc.getTestId(), 1, Integer::sum);

                if (tc.getStatus() == TestStatus.FAILED || tc.getStatus() == TestStatus.ERROR) {
                    FailureType type = (tc.getFailureType() == null) ? FailureType.UNKNOWN : tc.getFailureType();
                    failureTypeCounts.merge(type, 1, Integer::sum);
                    failuresByTestId.merge(tc.getTestId(), 1, Integer::sum);
                }
            }
        }

        Map<String, Double> failureRateByTestId = new HashMap<>();
        for (Map.Entry<String, Integer> entry : executionsByTestId.entrySet()) {
            String testId = entry.getKey();
            int execCount = entry.getValue();
            int failCount = failuresByTestId.getOrDefault(testId, 0);
            double rate = execCount == 0 ? 0.0 : (double) failCount / (double) execCount;
            failureRateByTestId.put(testId, rate);
        }

        return new AnalysisResult(
                totalRuns,
                totalTests,
                statusCounts,
                failureTypeCounts,
                failuresByTestId,
                failureRateByTestId
        );
    }
}
