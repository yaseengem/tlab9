package com.tlab9.live.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        return subjectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Subject createSubject(@RequestBody Subject subject) {
        return subjectRepository.save(subject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long id, @RequestBody Subject subjectDetails) {
        return subjectRepository.findById(id)
                .map(existingSubject -> {
                    updateFields(existingSubject, subjectDetails);
                    Subject updatedSubject = subjectRepository.save(existingSubject);
                    return ResponseEntity.ok(updatedSubject);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        return subjectRepository.findById(id)
                .map(subject -> {
                    subjectRepository.delete(subject);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private void updateFields(Subject existingSubject, Subject subjectDetails) {
        Field[] fields = Subject.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("subject_id")) { // Skip the primary key field
                    Object newValue = field.get(subjectDetails);
                    if (newValue != null) {
                        field.set(existingSubject, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
    }

    @PostMapping("/search")
    public List<Subject> searchSubjects(@RequestBody Map<String, String> searchParams) {
        String field = searchParams.get("field");
        String searchTerm = searchParams.get("searchTerm");

        Specification<Subject> spec = (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + searchTerm.toLowerCase() + "%");

        return subjectRepository.findAll(spec);
    }
}