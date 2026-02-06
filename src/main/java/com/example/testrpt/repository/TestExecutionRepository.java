package com.example.testrpt.repository;

import com.example.testrpt.model.TestExecutionRun;

import java.util.List;

public interface TestExecutionRepository {
    List<TestExecutionRun> loadRuns(String path);
}
