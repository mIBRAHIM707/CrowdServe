package com.crowdserve.dto;

import com.crowdserve.model.TaskStatus;

public record TaskDto(
    Long id,
    String title,
    String description,
    String location,
    Double reward,
    TaskStatus status,
    Long posterId,
    Long workerId
) {
}
