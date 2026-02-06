package com.example.testrpt;

import com.example.testrpt.repository.FileTestExecutionRepository;
import com.example.testrpt.repository.TestExecutionRepository;
import com.example.testrpt.service.TestExecutionAnalyzer;
import com.example.testrpt.service.TestExecutionValidator;
import com.example.testrpt.service.TestReportService;
import com.example.testrpt.service.ValidationError;
import com.example.testrpt.reporting.ReportGenerator;
import com.example.testrpt.reporting.ReportWriter;

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
        TestExecutionAnalyzer analyzer = new TestExecutionAnalyzer();
        TestReportService service = new TestReportService(
                repository,
                validator,
                analyzer,
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
