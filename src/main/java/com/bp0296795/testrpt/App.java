package com.bp0296795.testrpt;

import com.bp0296795.testrpt.repository.FileTestExecutionRepository;
import com.bp0296795.testrpt.repository.TestExecutionRepository;
import com.bp0296795.testrpt.service.TestExecutionAnalyser;
import com.bp0296795.testrpt.service.TestExecutionValidator;
import com.bp0296795.testrpt.service.TestReportService;
import com.bp0296795.testrpt.service.ValidationError;
import com.bp0296795.testrpt.reporting.ReportGenerator;
import com.bp0296795.testrpt.reporting.ReportWriter;

public class App {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar testrpt.jar <input-json-path> [output-report-path]");
            System.exit(1);
        }

        String inputPath = args[0];
        String outputPath = (args.length >= 2) ? args[1] : "test-execution-report.txt";

        TestExecutionRepository repository = new FileTestExecutionRepository();
        TestExecutionValidator validator = new TestExecutionValidator();
        TestExecutionAnalyser Analyser = new TestExecutionAnalyser();
        TestReportService service = new TestReportService(
                repository,
                validator,
                Analyser,
                new ReportGenerator(),
                new ReportWriter()
        );

        try {
            TestReportService.ServiceResult result = service.run(inputPath, outputPath);
            if (!result.isValid()) {
                System.err.println("Input validation failed. Fix the following issues:");
                for (ValidationError error : result.getValidation().getErrors()) {
                    System.err.println("- " + error.getField() + ": " + error.getMessage());
                }
                System.exit(2);
            }
            System.out.println("Report written to: " + result.getOutputPath());
        } catch (RuntimeException e) {
            System.err.println("Fatal error: " + e.getMessage());
            System.exit(3);
        }
    }
}
