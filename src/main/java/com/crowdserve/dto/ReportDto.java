package com.crowdserve.dto;

import com.crowdserve.model.ReportType;
import java.time.LocalDateTime;

public record ReportDto(
    Long id,
    ReportType type,
    LocalDateTime generatedDate,
    String data
) {
}
