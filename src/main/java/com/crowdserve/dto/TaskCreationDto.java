package com.crowdserve.dto;

/**
 * Data Transfer Object for task creation.
 * Contains the minimal required information to create a new task.
 */
public record TaskCreationDto(
    String title,
    String description,
    String location,
    Double reward
) {
}
