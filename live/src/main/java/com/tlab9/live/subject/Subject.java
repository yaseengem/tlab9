package com.tlab9.live.subject;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Table(name = "subjects")
@Schema(description = "Subject entity representing a subject in the system")
@Data
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the subject", example = "1")
    private Long subject_id;

    @Column(nullable = true, length = 255)
    @Schema(description = "Name of the subject", example = "Mathematics")
    private String subject_name;

    @Column(nullable = true)
    @Schema(description = "Introduction of the subject", example = "This subject covers basic to advanced mathematics.")
    private String intro;

    @Column(nullable = true)
    @Schema(description = "Content of the course", example = "Detailed content of the course goes here.")
    private String content;

    @Column(nullable = true)
    @Schema(description = "Sequence number of the subject", example = "1")
    private Integer seq_no;

    @Column(nullable = true)
    @Schema(description = "Creation timestamp of the subject")
    private LocalDateTime created_at;

    @Column(nullable = true)
    @Schema(description = "Last update timestamp of the subject")
    private LocalDateTime updated_at;

    @Column(nullable = true)
    @Schema(description = "Creator of the subject", example = "Jane Doe")
    private String created_by;

    @Column(nullable = true)
    @Schema(description = "URL of the head video for the subject", example = "http://example.com/video.mp4")
    private String head_video_url;

    @Column(nullable = true)
    @Schema(description = "URL of the head image for the subject", example = "http://example.com/image.jpg")
    private String head_image_url;

    @Column(nullable = true)
    @Schema(description = "Identifier of the course associated with the subject", example = "1")
    private Long course_id;

    @Column(nullable = true)
    @Schema(description = "Code of the subject", example = "MATH101")
    private String subject_code;

    @Column(nullable = true)
    @Schema(description = "Stage of the subject", example = "1.00000")
    private BigDecimal stage;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the subject is active", example = "false")
    private boolean is_active;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the subject is deleted", example = "false")
    private boolean is_deleted;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }
}