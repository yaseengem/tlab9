package com.tlab9.live.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Course", description = "API for managing courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Operation(summary = "Get all courses")
    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Operation(summary = "Get a course by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new course")
    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }

    @Operation(summary = "Update an existing course")
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                    updateFields(existingCourse, courseDetails);
                    Course updatedCourse = courseRepository.save(existingCourse);
                    return ResponseEntity.ok(updatedCourse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete an existing course")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(course -> {
                    courseRepository.delete(course);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private void updateFields(Course existingCourse, Course courseDetails) {
        Field[] fields = Course.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("course_id")) { // Skip the primary key field
                    Object newValue = field.get(courseDetails);
                    if (newValue != null) {
                        field.set(existingCourse, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
    }

    @Operation(summary = "Search for the courses based on a field and search term")
    @PostMapping("/search")
    public List<Course> searchCourses(@RequestBody Map<String, String> searchParams) {
        String field = searchParams.get("field");
        String searchTerm = searchParams.get("searchTerm");

        Specification<Course> spec = (root, query, cb) -> cb.like(cb.lower(root.get(field)),
                "%" + searchTerm.toLowerCase() + "%");

        return courseRepository.findAll(spec);
    }
}