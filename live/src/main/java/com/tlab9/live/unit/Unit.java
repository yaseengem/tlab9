package com.tlab9.live.unit;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Table(name = "units")
@Schema(description = "Unit entity representing a unit in the system")
@Data
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the unit", example = "1")
    private Long unit_id;

    @Column(nullable = true, length = 255)
    @Schema(description = "Name of the unit", example = "Unit 1")
    private String unit_name;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Introduction of the unit", example = "This unit covers the basics of the topic.")
    private String intro;

    @Column
    @Schema(description = "Sequence number of the unit", example = "1")
    private Integer seq_no;

    @Column(nullable = true)
    @Schema(description = "Creation timestamp of the unit")
    private LocalDateTime created_at;

    @Column(nullable = true)
    @Schema(description = "Last update timestamp of the unit")
    private LocalDateTime updated_at;

    @Column(nullable = true, columnDefinition = "TEXT")
    @Schema(description = "Creator of the unit", example = "Admin")
    private String created_by;

    @Column(length = 255)
    @Schema(description = "URL of the head video for the unit", example = "http://example.com/unit1.mp4")
    private String head_video_url;

    @Column(length = 255)
    @Schema(description = "URL of the head image for the unit", example = "http://example.com/unit1.jpg")
    private String head_image_url;

    @Column
    @Schema(description = "Identifier of the module associated with the unit", example = "1")
    private Long module_id;

    @Column(nullable = true, length = 50)
    @Schema(description = "Code of the unit", example = "U001")
    private String unit_code;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the unit is active", example = "false")
    private boolean is_active;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the unit is deleted", example = "false")
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