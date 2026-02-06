package com.example.testrpt.service;

import com.example.testrpt.model.FailureType;
import com.example.testrpt.model.TestCaseResult;
import com.example.testrpt.model.TestExecutionRun;
import com.example.testrpt.model.TestStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestExecutionValidatorTest {
    private final TestExecutionValidator validator = new TestExecutionValidator();

    @Test
    void validRunPassesValidation() {
        TestExecutionRun run = new TestExecutionRun(
                "run-1",
                "payments",
                "staging",
                "ci",
                LocalDateTime.parse("2026-02-06T10:00:00"),
                LocalDateTime.parse("2026-02-06T10:05:00"),
                List.of(new TestCaseResult(
                        "T-1",
                        "should authorize",
                        TestStatus.PASSED,
                        1200,
                        null,
                        null,
                        Set.of("smoke")
                ))
        );

        ValidationResult result = validator.validateRuns(List.of(run));
        assertTrue(result.isValid());
    }

    @Test
    void missingRequiredFieldsFailsValidation() {
        TestExecutionRun run = new TestExecutionRun(
                null,
                "",
                null,
                "ci",
                null,
                null,
                List.of(new TestCaseResult(
                        null,
                        "",
                        null,
                        -1,
                        null,
                        null,
                        Set.of()
                ))
        );

        ValidationResult result = validator.validateRuns(List.of(run));
        assertFalse(result.isValid());
    }

    @Test
    void failedTestRequiresFailureDetails() {
        TestExecutionRun run = new TestExecutionRun(
                "run-2",
                "payments",
                "staging",
                "ci",
                LocalDateTime.parse("2026-02-06T11:00:00"),
                LocalDateTime.parse("2026-02-06T11:05:00"),
                List.of(new TestCaseResult(
                        "T-2",
                        "should reject expired",
                        TestStatus.FAILED,
                        900,
                        null,
                        "",
                        Set.of()
                ))
        );

        ValidationResult result = validator.validateRuns(List.of(run));
        assertFalse(result.isValid());
    }

    @Test
    void nonFailingTestShouldNotProvideFailureType() {
        TestExecutionRun run = new TestExecutionRun(
                "run-3",
                "payments",
                "staging",
                "ci",
                LocalDateTime.parse("2026-02-06T12:00:00"),
                LocalDateTime.parse("2026-02-06T12:05:00"),
                List.of(new TestCaseResult(
                        "T-3",
                        "should process",
                        TestStatus.PASSED,
                        500,
                        FailureType.ASSERTION,
                        null,
                        Set.of()
                ))
        );

        ValidationResult result = validator.validateRuns(List.of(run));
        assertFalse(result.isValid());
    }

    @Test
    void duplicateIdsAreRejected() {
        TestCaseResult tc1 = new TestCaseResult(
                "T-4",
                "test A",
                TestStatus.PASSED,
                100,
                null,
                null,
                Set.of()
        );
        TestCaseResult tc2 = new TestCaseResult(
                "T-4",
                "test B",
                TestStatus.FAILED,
                200,
                FailureType.ASSERTION,
                "boom",
                Set.of()
        );
        TestExecutionRun run1 = new TestExecutionRun(
                "run-4",
                "suite",
                "staging",
                "ci",
                LocalDateTime.parse("2026-02-06T13:00:00"),
                LocalDateTime.parse("2026-02-06T13:10:00"),
                List.of(tc1, tc2)
        );
        TestExecutionRun run2 = new TestExecutionRun(
                "run-4",
                "suite",
                "staging",
                "ci",
                LocalDateTime.parse("2026-02-06T13:20:00"),
                LocalDateTime.parse("2026-02-06T13:30:00"),
                List.of(tc1)
        );

        ValidationResult result = validator.validateRuns(List.of(run1, run2));
        assertFalse(result.isValid());
    }
}
