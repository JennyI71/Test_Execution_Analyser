package com.example.testrpt.service;

import com.example.testrpt.model.AnalysisResult;
import com.example.testrpt.model.TestExecutionRun;
import com.example.testrpt.reporting.ReportGenerator;
import com.example.testrpt.reporting.ReportWriter;
import com.example.testrpt.repository.TestExecutionRepository;

import java.util.List;

public class TestReportService {
    private final TestExecutionRepository repository;
    private final TestExecutionValidator validator;
    private final TestExecutionAnalyser Analyser;
    private final ReportGenerator generator;
    private final ReportWriter writer;

    public TestReportService(TestExecutionRepository repository,
                             TestExecutionValidator validator,
                             TestExecutionAnalyser Analyser,
                             ReportGenerator generator,
                             ReportWriter writer) {
        this.repository = repository;
        this.validator = validator;
        this.Analyser = Analyser;
        this.generator = generator;
        this.writer = writer;
    }

    public ServiceResult run(String inputPath, String outputPath) {
        List<TestExecutionRun> runs = repository.loadRuns(inputPath);
        ValidationResult validation = validator.validateRuns(runs);
        if (!validation.isValid()) {
            return ServiceResult.validationFailed(validation);
        }

        AnalysisResult result = Analyser.Analyse(runs);
        String report = generator.generate(result, runs);
        writer.writeToConsole(report);
        String resolvedPath = writer.writeToFile(report, outputPath);
        return ServiceResult.success(report, resolvedPath);
    }

    public static final class ServiceResult {
        private final ValidationResult validation;
        private final String report;
        private final String outputPath;

        private ServiceResult(ValidationResult validation, String report, String outputPath) {
            this.validation = validation;
            this.report = report;
            this.outputPath = outputPath;
        }

        public static ServiceResult validationFailed(ValidationResult validation) {
            return new ServiceResult(validation, null, null);
        }

        public static ServiceResult success(String report, String outputPath) {
            return new ServiceResult(new ValidationResult(), report, outputPath);
        }

        public boolean isValid() {
            return validation == null || validation.isValid();
        }

        public ValidationResult getValidation() {
            return validation;
        }

        public String getReport() {
            return report;
        }

        public String getOutputPath() {
            return outputPath;
        }
    }
}
