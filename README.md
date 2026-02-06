# Test Execution Analysis and Reporting Tool

Standalone Java CLI that ingests test execution runs from JSON, Analyses results, and generates a report.

## Quick Start (Run the Sample)
```powershell
cd Test_Execution_Analyser
mvn -q -DskipTests package
mvn -q --% exec:java -Dexec.mainClass=com.example.testrpt.App -Dexec.args=".\src\main\resources\sample-runs.json .\report.txt"
mvn test
```

Outputs:
- Report written to `reports\` with a timestamped name.
- Unit test reports in `target/surefire-reports/`.

## Prerequisites
- Java 17 (JDK) on `PATH`
- Maven 3.8+ on `PATH`
- Windows PowerShell (commands below)

## Project Structure
- `src/main/java/com/example/testrpt/App.java`: CLI entry point (parses args, calls the service).
- `src/main/java/com/example/testrpt/service/TestReportService.java`: Orchestrates load -> validate -> Analyse -> report -> write.
- `src/main/java/com/example/testrpt/repository/FileTestExecutionRepository.java`: Loads runs from JSON file.
- `src/main/java/com/example/testrpt/service/TestExecutionValidator.java`: Validates input data and business rules.
- `src/main/java/com/example/testrpt/service/TestExecutionAnalyser.java`: Aggregates counts and failure rates.
- `src/main/java/com/example/testrpt/reporting/ReportGenerator.java`: Builds the human-readable report string.
- `src/main/java/com/example/testrpt/reporting/ReportWriter.java`: Writes report to console and timestamped file in `reports/`.
- `src/main/resources/sample-runs.json`: Sample input to run the app quickly.
- `src/test/java/...`: Unit tests.

## Run (Custom Input)
```powershell
mvn -q --% exec:java -Dexec.mainClass=com.example.testrpt.App -Dexec.args="C:\path\to\input.json C:\path\to\report.txt"
```

The output is always timestamped and written into a `reports` folder, e.g. `reports/report-20260206-143010.txt`.

## What It Does
- Validates input JSON and required fields.
- Aggregates totals, status counts, failure counts, and failure rates.
- Prints the report to console and writes it to a file.

## Input JSON
Use the bundled sample at `src/main/resources/sample-runs.json`.

Shape (simplified):
- Run: `runId`, `suiteName`, `environment`, `triggeredBy`, `startTime`, `endTime`, `results[]`
- Test case: `testId`, `name`, `status`, `durationMillis`, optional `failureType`, `failureMessage`, `tags[]`

## Tests
```powershell
mvn test
```
Unit tests use TestNG.
Reports are generated under `target/surefire-reports/`.

## Why It's Useful (For Test Automation Engineers)
This tool acts as a lightweight reporting step after your automated suite runs. Instead of digging through raw test output, it gives you a clear, consistent summary that can be shared with teams or attached to CI artifacts.

What it provides:
- A readable report with totals (runs/tests).
- Status counts (PASSED/FAILED/SKIPPED/ERROR).
- Failure type breakdowns (e.g., assertion vs timeout).
- A list of the most frequently failing tests with failure rates.
- A run overview with environment and timing details.

Why that helps:
- Faster triage of failing areas.
- Easier spotting of flaky or consistently failing tests.
- Quick reporting for stakeholders without opening log files.

---

**Author:** BP0296795  
**Version:** v1.0.0
