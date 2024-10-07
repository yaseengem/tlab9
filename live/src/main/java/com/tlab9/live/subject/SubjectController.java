package com.tlab9.live.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
@Tag(name = "Subject", description = "API for managing subjects")
@Slf4j
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @Operation(summary = "Get all subjects")
    @GetMapping
    public List<Subject> getAllSubjects() {
        log.info("Entering getAllSubjects method");
        List<Subject> subjects = subjectRepository.findAll();
        log.info("Successfully found {} subjects", subjects.size());
        log.info("Exiting getAllSubjects method");
        return subjects;
    }

    @Operation(summary = "Get a subject by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        log.info("Entering getSubjectById method with id: {}", id);
        return subjectRepository.findById(id)
                .map(subject -> {
                    log.info("Subject found with id: {}", id);
                    return ResponseEntity.ok(subject);
                })
                .orElseGet(() -> {
                    log.warn("Subject with id {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Operation(summary = "Create a new subject")
    @PostMapping
    public Subject createSubject(@RequestBody Subject subject) {
        log.info("Entering createSubject method with subject: {}", subject);
        Subject createdSubject = subjectRepository.save(subject);
        log.info("Successfully created subject: {}", createdSubject);
        log.info("Exiting createSubject method");
        return createdSubject;
    }

    @Operation(summary = "Update a subject by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long id, @RequestBody Subject subjectDetails) {
        log.info("Entering updateSubject method with id: {} and subjectDetails: {}", id, subjectDetails);
        return subjectRepository.findById(id)
                .map(existingSubject -> {
                    log.info("Subject found with id: {}", id);
                    updateFields(existingSubject, subjectDetails);
                    Subject updatedSubject = subjectRepository.save(existingSubject);
                    log.info("Successfully updated subject: {}", updatedSubject);
                    return ResponseEntity.ok(updatedSubject);
                })
                .orElseGet(() -> {
                    log.warn("Subject with id {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Operation(summary = "Delete a subject by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        log.info("Entering deleteSubject method with id: {}", id);
        ResponseEntity<Void> response = subjectRepository.findById(id)
                .map(subject -> {
                    subjectRepository.delete(subject);
                    log.info("Exiting deleteSubject method after deleting subject with id: {}", id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> {
                    log.warn("Subject with id: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Exiting deleteSubject method with response: {}", response);
        return response;
    }

    private void updateFields(Subject existingSubject, Subject subjectDetails) {
        log.info("Entering updateFields method");
        Field[] fields = Subject.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("subject_id")) { // Skip the primary key field
                    Object newValue = field.get(subjectDetails);
                    if (newValue != null) {
                        log.info("Updating field: {} with new value: {}", field.getName(), newValue);
                        field.set(existingSubject, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("Failed to update field: {}", field.getName(), e);
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
        log.info("Exiting updateFields method");
    }

    @Operation(summary = "Search for the subjects based on a field and search term")
    @PostMapping("/search")
    public List<Subject> searchSubjects(@RequestBody Map<String, String> searchParams) {
        log.info("Entering searchSubjects method with searchParams: {}", searchParams);
        String field = searchParams.get("field");
        String searchTerm = searchParams.get("searchTerm");

        Specification<Subject> spec;
        if (field != null && !field.isEmpty()) {
            log.info("Searching subjects by field: {} with searchTerm: {}", field, searchTerm);
            spec = (root, query, cb) -> cb.like(cb.lower(root.get(field)),
                    "%" + searchTerm.toLowerCase() + "%");
        } else {
            log.info("Searching subjects by default fields with searchTerm: {}", searchTerm);
            spec = (root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("subject_name")), "%" + searchTerm.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("intro")), "%" + searchTerm.toLowerCase() + "%"));
        }

        List<Subject> subjects = subjectRepository.findAll(spec);
        log.info("Successfully found {} subjects", subjects.size());
        log.info("Exiting searchSubjects method");
        return subjects;
    }
}