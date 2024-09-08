package com.tlab9.live.subject;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subject_id")
    private Long subject_id;

    @Column(name = "description")
    private String description;

    @Column(name = "sequence_no")
    private Integer sequence_no;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "created_by")
    private String created_by;

    @Column(name = "head_video_url", length = 255)
    private String head_video_url;

    @Column(name = "head_image_url", length = 255)
    private String head_image_url;

    // Getters and setters
    public Long getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(Long subject_id) {
        this.subject_id = subject_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSequence_no() {
        return sequence_no;
    }

    public void setSequence_no(Integer sequence_no) {
        this.sequence_no = sequence_no;
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

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
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