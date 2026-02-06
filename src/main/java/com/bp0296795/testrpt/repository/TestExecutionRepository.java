package com.bp0296795.testrpt.repository;

import com.bp0296795.testrpt.model.TestExecutionRun;

import java.util.List;

public interface TestExecutionRepository {
    List<TestExecutionRun> loadRuns(String path);
}
