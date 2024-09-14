package com.tlab9.live.course;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "courses")
@Schema(description = "Course entity representing a course in the system")
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

    // Getters and setters...



    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }
    public Long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getHead_video_url() {
        return head_video_url;
    }

    public void setHead_video_url(String head_video_url) {
        this.head_video_url = head_video_url;
    }

    public String getHead_image_url() {
        return head_image_url;
    }

    public void setHead_image_url(String head_image_url) {
        this.head_image_url = head_image_url;
    }
}