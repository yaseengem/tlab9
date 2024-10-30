package com.tlab9.live.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Table(name = "users")
@Schema(description = "User entity representing a user in the system")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long user_id;

    @Column(nullable = true, length = 255)
    @Schema(description = "Name of the user", example = "John Doe")
    private String user_name;

    @Column(nullable = false, length = 255)
    @Schema(description = "Email of the user", example = "john.doe@example.com")
    private String email;

    @Column(nullable = true, length = 50)
    @Schema(description = "Role of the user", example = "Admin")
    private String role;

    @Column(nullable = true, length = 50)
    @Schema(description = "Code of the user", example = "USR001")
    private String user_code;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Introduction of the user", example = "This is a brief introduction about the user.")
    private String intro;

    @Column(nullable = true)
    @Schema(description = "Profile of the User", example = "Detailed Profile of the user goes here.")
    private String content;

    @Column(nullable = true)
    @Schema(description = "Creation timestamp of the user")
    private LocalDateTime created_at;

    @Column(nullable = true)
    @Schema(description = "Last update timestamp of the user")
    private LocalDateTime updated_at;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the user is active", example = "false")
    private boolean is_active;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Schema(description = "Indicates if the user is deleted", example = "false")
    private boolean is_deleted;

    @Column(length = 255)
    @Schema(description = "URL of the head video for the user", example = "http://example.com/video.mp4")
    private String head_video_url;

    @Column(length = 255)
    @Schema(description = "URL of the head image for the user", example = "http://example.com/image.jpg")
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