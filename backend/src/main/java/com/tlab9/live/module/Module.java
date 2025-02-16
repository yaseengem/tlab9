package com.tlab9.live.module;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Table(name = "modules")
@Schema(description = "Module entity representing a module in the system")
@Data
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the module", example = "1")
    private Long module_id;

    @Column(nullable = true)
    @Schema(description = "Name of the module", example = "Introduction to Programming")
    private String module_name;

    @Column(nullable = true)
    @Schema(description = "Introduction of the module", example = "This module covers the basics of programming.")
    private String intro;

    @Column(nullable = true)
    @Schema(description = "Content of the course", example = "Detailed content of the course goes here.")
    private String content;

    @Column(nullable = true)
    @Schema(description = "Sequence number of the module", example = "1")
    private Integer seq_no;

    @Column(nullable = true)
    @Schema(description = "Creation timestamp of the module")
    private LocalDateTime created_at;

    @Column(nullable = true)
    @Schema(description = "Last update timestamp of the module")
    private LocalDateTime updated_at;

    @Column(nullable = true)
    @Schema(description = "Creator of the module", example = "Jane Doe")
    private String created_by;

    @Column(length = 255)
    @Schema(description = "URL of the head video for the module", example = "http://example.com/video.mp4")
    private String head_video_url;

    @Column(length = 255)
    @Schema(description = "URL of the head image for the module", example = "http://example.com/image.jpg")
    private String head_image_url;

    @Column
    @Schema(description = "Identifier of the subject associated with the module", example = "1")
    private Long subject_id;

    @Column(nullable = true, length = 50)
    @Schema(description = "Code of the module", example = "CS101")
    private String module_code;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the module is active", example = "false")
    private boolean is_active;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the module is deleted", example = "false")
    private boolean is_deleted;

    @Column(nullable = true)
    @Schema(description = "Stage of the module", example = "1.00000")
    private BigDecimal stage;

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