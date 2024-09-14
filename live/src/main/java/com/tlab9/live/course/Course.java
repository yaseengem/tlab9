package com.tlab9.live.course;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Table(name = "courses")
@Schema(description = "Course entity representing a course in the system")
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the course", example = "1")
    private Long course_id;

    @Column(nullable = false)
    @Schema(description = "Name of the course", example = "Introduction to Programming")
    private String course_name;

    @Column(nullable = false)
    @Schema(description = "Code of the course", example = "CS101")
    private String course_code;

    @Column(nullable = true, columnDefinition = "TEXT")
    @Schema(description = "Description of the course", example = "This course covers the basics of programming.")
    private String description;

    @Column(nullable = true, columnDefinition = "TEXT")
    @Schema(description = "Creator of the course", example = "John Doe")
    private String created_by;

    @Column(nullable = true)
    @Schema(description = "Creation timestamp of the course")
    private LocalDateTime created_at;

    @Column(nullable = true)
    @Schema(description = "Last update timestamp of the course")
    private LocalDateTime updated_at;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the course is active", example = "false")
    private boolean is_active;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the course is deleted", example = "false")
    private boolean is_deleted;

    @Column(name = "head_video_url", length = 255)
    @Schema(description = "URL of the head video for the course", example = "http://example.com/video.mp4")
    private String head_video_url;

    @Column(name = "head_image_url", length = 255)
    @Schema(description = "URL of the head image for the course", example = "http://example.com/image.jpg")
    private String head_image_url;

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