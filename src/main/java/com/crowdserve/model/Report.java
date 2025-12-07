package com.crowdserve.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a generated report.
 */
@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Column(nullable = false)
    private LocalDateTime generatedDate;

    @ManyToOne
    @JoinColumn(name = "generated_by_id")
    private User generatedBy;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String data; // Storing JSON or simple text for now
}
