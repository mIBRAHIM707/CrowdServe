package com.crowdserve.service;

/**
 * Service interface defining reporting and analytics operations.
 * Provides contract for generating various reports related to tasks and users.
 */
public interface ReportService {
    
    /**
     * Generates a report of all completed tasks.
     *
     * @return byte array representing the report (e.g., PDF, CSV, etc.)
     */
    byte[] generateCompletedTasksReport();
    
    /**
     * Generates a report of top contributors based on task completion.
     *
     * @return byte array representing the report (e.g., PDF, CSV, etc.)
     */
    byte[] generateTopContributorsReport();
}
