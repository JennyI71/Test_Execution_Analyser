package com.bp0296795.testrpt.reporting;

import com.bp0296795.testrpt.model.AnalysisResult;
import com.bp0296795.testrpt.model.FailureType;
import com.bp0296795.testrpt.model.TestExecutionRun;
import com.bp0296795.testrpt.model.TestStatus;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String generate(AnalysisResult result, List<TestExecutionRun> runs) {
        StringBuilder sb = new StringBuilder();
        appendTitle(sb, "Test Execution Analysis Report");
        appendKeyValue(sb, "Runs", result.getTotalRuns());
        appendKeyValue(sb, "Tests", result.getTotalTests());
        sb.append(System.lineSeparator());

        appendSectionHeader(sb, "Status Counts");
        for (TestStatus status : TestStatus.values()) {
            int count = result.getStatusCounts().getOrDefault(status, 0);
            sb.append("- ").append(status).append(": ").append(count).append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());

        appendSectionHeader(sb, "Failure Types");
        for (FailureType type : FailureType.values()) {
            int count = result.getFailureTypeCounts().getOrDefault(type, 0);
            sb.append("- ").append(type).append(": ").append(count).append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());

        appendSectionHeader(sb, "Top Failing Tests");
        List<Map.Entry<String, Integer>> topFailures = result.getFailuresByTestId()
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList());
        if (topFailures.isEmpty()) {
            sb.append("- None").append(System.lineSeparator());
        } else {
            for (Map.Entry<String, Integer> entry : topFailures) {
                double rate = result.getFailureRateByTestId().getOrDefault(entry.getKey(), 0.0);
                sb.append("- ").append(entry.getKey())
                        .append(": ").append(entry.getValue())
                        .append(" failures (rate ").append(String.format("%.2f", rate * 100)).append("%)")
                        .append(System.lineSeparator());
            }
        }
        sb.append(System.lineSeparator());

        appendSectionHeader(sb, "Run Overview");
        for (TestExecutionRun run : runs) {
            sb.append("- ").append(run.getRunId())
                    .append(" | ").append(run.getSuiteName())
                    .append(" | ").append(run.getEnvironment())
                    .append(" | ").append(run.getStartTime().format(TS_FORMAT))
                    .append(" -> ").append(run.getEndTime().format(TS_FORMAT))
                    .append(" | tests: ").append(run.getResults().size())
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }

    private void appendTitle(StringBuilder sb, String title) {
        sb.append(title).append(System.lineSeparator());
        sb.append("=".repeat(title.length())).append(System.lineSeparator());
    }

    private void appendSectionHeader(StringBuilder sb, String title) {
        sb.append(title).append(System.lineSeparator());
    }

    private void appendKeyValue(StringBuilder sb, String label, int value) {
        sb.append(label).append(": ").append(value).append(System.lineSeparator());
    }
}
