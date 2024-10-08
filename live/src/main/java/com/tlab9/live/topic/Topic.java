package com.tlab9.live.topic;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @Column(nullable = true, length = 255)
    @Schema(description = "Name of the topic", example = "Introduction to Java")
    private String topic_name;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Introduction of the topic", example = "This topic covers the basics of Java programming.")
    private String intro;

    @Column(nullable = true, length = 50)
    @Schema(description = "Type of the topic", example = "Lecture")
    private String topic_type;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Content of the topic", example = "Detailed content of the topic goes here.")
    private String content;

    @Column
    @Schema(description = "Sequence number of the topic", example = "1")
    private Integer seq_no;

    @Column(nullable = true)
    @Schema(description = "Creation timestamp of the topic")
    private LocalDateTime created_at;

    @Column(nullable = true)
    @Schema(description = "Last update timestamp of the topic")
    private LocalDateTime updated_at;

    @Column(nullable = true, columnDefinition = "TEXT")
    @Schema(description = "Creator of the topic", example = "John Doe")
    private String created_by;

    @Column(length = 255)
    @Schema(description = "URL of the head video for the topic", example = "http://example.com/video.mp4")
    private String head_video_url;

    @Column(length = 255)
    @Schema(description = "URL of the head image for the topic", example = "http://example.com/image.jpg")
    private String head_image_url;

    @Column
    @Schema(description = "Identifier of the module associated with the topic", example = "1")
    private Long unit_id;

    @Column(nullable = true, length = 50)
    @Schema(description = "Code of the topic", example = "JAVA101")
    private String topic_code;

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