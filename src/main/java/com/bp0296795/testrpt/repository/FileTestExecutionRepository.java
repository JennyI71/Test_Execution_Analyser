package com.bp0296795.testrpt.repository;

import com.bp0296795.testrpt.model.TestExecutionRun;
import com.bp0296795.testrpt.util.JsonMapperFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileTestExecutionRepository implements TestExecutionRepository {
    private final ObjectMapper mapper;

    public FileTestExecutionRepository() {
        this.mapper = JsonMapperFactory.create();
    }

    @Override
    public List<TestExecutionRun> loadRuns(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Input path must be provided.");
        }
        Path file = Path.of(path);
        if (!Files.exists(file)) {
            throw new IllegalArgumentException("Input file does not exist: " + path);
        }
        if (!Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Input path is not a file: " + path);
        }
        try {
            return mapper.readValue(file.toFile(), new TypeReference<List<TestExecutionRun>>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read or parse JSON input: " + path, e);
        }
    }
}
