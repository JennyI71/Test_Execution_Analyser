package com.example.testrpt.service;

import com.example.testrpt.model.AnalysisResult;
import com.example.testrpt.model.FailureType;
import com.example.testrpt.model.TestCaseResult;
import com.example.testrpt.model.TestExecutionRun;
import com.example.testrpt.model.TestStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestExecutionAnalyzerTest {
    private final TestExecutionAnalyzer analyzer = new TestExecutionAnalyzer();

    @Test
    void aggregatesStatusAndFailureCounts() {
        TestExecutionRun run = new TestExecutionRun(
                "run-1",
                "payments",
                "staging",
                "ci",
                LocalDateTime.parse("2026-02-06T10:00:00"),
                LocalDateTime.parse("2026-02-06T10:05:00"),
                List.of(
                        new TestCaseResult("T-1", "A", TestStatus.PASSED, 100, null, null, Set.of()),
                        new TestCaseResult("T-2", "B", TestStatus.FAILED, 120, FailureType.ASSERTION, "oops", Set.of()),
                        new TestCaseResult("T-3", "C", TestStatus.ERROR, 130, FailureType.INFRASTRUCTURE, "env", Set.of())
                )
        );

        AnalysisResult result = analyzer.analyze(List.of(run));
        assertEquals(1, result.getTotalRuns());
        assertEquals(3, result.getTotalTests());
        assertEquals(1, result.getStatusCounts().get(TestStatus.PASSED));
        assertEquals(1, result.getStatusCounts().get(TestStatus.FAILED));
        assertEquals(1, result.getStatusCounts().get(TestStatus.ERROR));
        assertEquals(1, result.getFailureTypeCounts().get(FailureType.ASSERTION));
        assertEquals(1, result.getFailureTypeCounts().get(FailureType.INFRASTRUCTURE));
    }

    @Test
    void calculatesFailureRatesAcrossRuns() {
        TestExecutionRun run1 = new TestExecutionRun(
                "run-1",
                "payments",
                "staging",
                "ci",
                LocalDateTime.parse("2026-02-06T10:00:00"),
                LocalDateTime.parse("2026-02-06T10:05:00"),
                List.of(
                        new TestCaseResult("T-1", "A", TestStatus.FAILED, 100, FailureType.ASSERTION, "oops", Set.of()),
                        new TestCaseResult("T-2", "B", TestStatus.PASSED, 100, null, null, Set.of())
                )
        );
        TestExecutionRun run2 = new TestExecutionRun(
                "run-2",
                "payments",
                "staging",
                "ci",
                LocalDateTime.parse("2026-02-06T11:00:00"),
                LocalDateTime.parse("2026-02-06T11:05:00"),
                List.of(
                        new TestCaseResult("T-1", "A", TestStatus.PASSED, 100, null, null, Set.of()),
                        new TestCaseResult("T-2", "B", TestStatus.PASSED, 100, null, null, Set.of())
                )
        );

        AnalysisResult result = analyzer.analyze(List.of(run1, run2));
        assertEquals(0.5, result.getFailureRateByTestId().get("T-1"));
        assertEquals(0.0, result.getFailureRateByTestId().get("T-2"));
        assertEquals(1, result.getFailuresByTestId().get("T-1"));
    }
}
