package com.bp0296795.testrpt.reporting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportWriter {
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    public void writeToConsole(String report) {
        System.out.println(report);
    }

    public String writeToFile(String report, String outputPath) {
        if (outputPath == null || outputPath.isBlank()) {
            throw new IllegalArgumentException("Output path must be provided.");
        }
        String resolvedPath = withTimestampInReportsDir(outputPath);
        try {
            Path resolved = Path.of(resolvedPath);
            Path parent = resolved.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(Path.of(resolvedPath), report);
            return resolvedPath;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write report to file: " + resolvedPath, e);
        }
    }

    private String withTimestampInReportsDir(String outputPath) {
        Path path = Path.of(outputPath);
        String filename = path.getFileName().toString();
        String ts = LocalDateTime.now().format(TS_FORMAT);

        int dot = filename.lastIndexOf('.');
        String stamped = (dot > 0)
                ? filename.substring(0, dot) + "-" + ts + filename.substring(dot)
                : filename + "-" + ts;

        Path parent = path.getParent();
        Path reportsDir = (parent == null) ? Path.of("reports") : parent.resolve("reports");
        return reportsDir.resolve(stamped).toString();
    }
}
