package com.tlab9.live.topic;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Table(name = "topics")
@Schema(description = "Topic entity representing a topic in the system")
@Data
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the topic", example = "1")
    private Long topic_id;

    @Column(nullable = true)
    @Schema(description = "Name of the topic", example = "Introduction to Java")
    private String topic_name;

    @Column(nullable = true)
    @Schema(description = "Introduction of the topic", example = "This topic covers the basics of Java programming.")
    private String intro;

    @Column(nullable = true)
    @Schema(description = "Type of the topic", example = "Lecture")
    private String topic_type;

    @Column(nullable = true)
    @Schema(description = "Content of the topic", example = "Detailed content of the topic goes here.")
    private String content;

    @Column(nullable = true)
    @Schema(description = "Sequence number of the topic", example = "1")
    private Integer seq_no;

    @Column(nullable = true)
    @Schema(description = "Creation timestamp of the topic")
    private LocalDateTime created_at;

    @Column(nullable = true)
    @Schema(description = "Last update timestamp of the topic")
    private LocalDateTime updated_at;

    @Column(nullable = true)
    @Schema(description = "Creator of the topic", example = "John Doe")
    private String created_by;

    @Column(nullable = true)
    @Schema(description = "URL of the head video for the topic", example = "http://example.com/video.mp4")
    private String head_video_url;

    @Column(nullable = true)
    @Schema(description = "URL of the head image for the topic", example = "http://example.com/image.jpg")
    private String head_image_url;

    @Column(nullable = true)
    @Schema(description = "Identifier of the module associated with the topic", example = "1")
    private Long unit_id;

    @Column(nullable = true)
    @Schema(description = "Code of the topic", example = "JAVA101")
    private String topic_code;

    @Column(nullable = true)
    @Schema(description = "Stage of the topic", example = "1.00000")
    private BigDecimal stage;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the topic is active", example = "false")
    private boolean is_active;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the topic is deleted", example = "false")
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