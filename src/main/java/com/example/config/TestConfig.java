package com.example.config;

public class TestConfig {
    private static String testIndexDir;

    public static void setTestIndexDir(String dir) {
        testIndexDir = dir;
    }

    public static String getIndexDir() {
        return testIndexDir != null ? testIndexDir : "./index";
    }
} 