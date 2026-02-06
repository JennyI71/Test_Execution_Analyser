package com.bp0296795.testrpt.service;

import com.bp0296795.testrpt.model.FailureType;
import com.bp0296795.testrpt.model.TestCaseResult;
import com.bp0296795.testrpt.model.TestExecutionRun;
import com.bp0296795.testrpt.model.TestStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestExecutionValidator {
    public ValidationResult validateRuns(List<TestExecutionRun> runs) {
        ValidationResult result = new ValidationResult();
        ValidationContext ctx = new ValidationContext(result);
        if (runs == null || runs.isEmpty()) {
            result.addError("runs", "At least one execution run is required.");
            return result;
        }

        Set<String> runIds = new HashSet<>();
        for (int i = 0; i < runs.size(); i++) {
            TestExecutionRun run = runs.get(i);
            String prefix = "runs[" + i + "]";
            if (run == null) {
                ctx.error(prefix, "Run entry must not be null.");
                continue;
            }
            ctx.requireNotBlank(run.getRunId(), prefix + ".runId");
            ctx.requireNotBlank(run.getSuiteName(), prefix + ".suiteName");
            ctx.requireNotBlank(run.getEnvironment(), prefix + ".environment");
            ctx.requireNotBlank(run.getTriggeredBy(), prefix + ".triggeredBy");
            validateTimeOrder(run.getStartTime(), run.getEndTime(), prefix, ctx);

            if (run.getRunId() != null && !runIds.add(run.getRunId())) {
                ctx.error(prefix + ".runId", "Duplicate runId detected: " + run.getRunId());
            }

            List<TestCaseResult> cases = run.getResults();
            if (cases == null || cases.isEmpty()) {
                ctx.error(prefix + ".results", "Each run must contain at least one test case result.");
                continue;
            }
            validateCases(cases, prefix, ctx);
        }
        return result;
    }

    private void validateCases(List<TestCaseResult> cases, String runPrefix, ValidationContext ctx) {
        Set<String> testIds = new HashSet<>();
        for (int i = 0; i < cases.size(); i++) {
            TestCaseResult tc = cases.get(i);
            String prefix = runPrefix + ".results[" + i + "]";
            if (tc == null) {
                ctx.error(prefix, "Test case entry must not be null.");
                continue;
            }
            ctx.requireNotBlank(tc.getTestId(), prefix + ".testId");
            ctx.requireNotBlank(tc.getName(), prefix + ".name");
            if (tc.getStatus() == null) {
                ctx.error(prefix + ".status", "Status is required.");
            }
            if (tc.getDurationMillis() < 0) {
                ctx.error(prefix + ".durationMillis", "Duration must be non-negative.");
            }

            if (tc.getTestId() != null && !testIds.add(tc.getTestId())) {
                ctx.error(prefix + ".testId", "Duplicate testId within run: " + tc.getTestId());
            }

            if (tc.getStatus() == TestStatus.FAILED || tc.getStatus() == TestStatus.ERROR) {
                if (tc.getFailureType() == null) {
                    ctx.error(prefix + ".failureType", "Failure type required for failed or error tests.");
                }
                if (isBlank(tc.getFailureMessage())) {
                    ctx.error(prefix + ".failureMessage", "Failure message required for failed or error tests.");
                }
            } else if (tc.getFailureType() != null && tc.getFailureType() != FailureType.UNKNOWN) {
                ctx.error(prefix + ".failureType", "Failure type should be omitted for non-failing tests.");
            }
        }
    }

    private void validateTimeOrder(LocalDateTime start, LocalDateTime end, String prefix, ValidationContext ctx) {
        if (start == null) {
            ctx.error(prefix + ".startTime", "Start time is required.");
            return;
        }
        if (end == null) {
            ctx.error(prefix + ".endTime", "End time is required.");
            return;
        }
        if (end.isBefore(start)) {
            ctx.error(prefix + ".endTime", "End time must be after start time.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static final class ValidationContext {
        private final ValidationResult result;

        private ValidationContext(ValidationResult result) {
            this.result = result;
        }

        private void error(String field, String message) {
            result.addError(field, message);
        }

        private void requireNotBlank(String value, String field) {
            if (value == null || value.isBlank()) {
                result.addError(field, "Value is required.");
            }
        }
    }
}
