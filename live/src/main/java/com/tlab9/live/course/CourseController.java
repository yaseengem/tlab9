package com.tlab9.live.course;

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
@RequestMapping("/api/course")
@Tag(name = "Course", description = "API for managing courses")
@Slf4j
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Operation(summary = "Get all courses")
    @GetMapping
    public List<Course> getAllCourses() {
        log.info("Entering getAllCourses method");
        List<Course> courses = null;
        try {
            courses = courseRepository.findAll();
            log.info("Successfully retrieved all courses");
        } catch (Exception e) {
            log.error("Error occurred while retrieving courses: ", e);
        }
        log.info("Exiting getAllCourses method");
        return courses;
    }

    @Operation(summary = "Get a course by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        log.info("Entering getCourseById method with id: {}", id);
        ResponseEntity<Course> response = courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        log.info("Exiting getCourseById method with response: {}", response);
        return response;
    }

    @Operation(summary = "Create a new course")
    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        log.info("Entering createCourse method with course: {}", course);
        Course savedCourse = courseRepository.save(course);
        log.info("Exiting createCourse method with saved course: {}", savedCourse);
        return savedCourse;
    }

    @Operation(summary = "Update an existing course")
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        log.info("Entering updateCourse method with id: {} and courseDetails: {}", id, courseDetails);
        ResponseEntity<Course> response = courseRepository.findById(id)
                .map(existingCourse -> {
                    updateFields(existingCourse, courseDetails);
                    Course updatedCourse = courseRepository.save(existingCourse);
                    log.info("Successfully updated course with id: {}", id);
                    return ResponseEntity.ok(updatedCourse);
                })
                .orElseGet(() -> {
                    log.warn("Course with id: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Exiting updateCourse method with response: {}", response);
        return response;
    }

    @Operation(summary = "Delete a course")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.info("Entering deleteCourse method with id: {}", id);
        ResponseEntity<Void> response = courseRepository.findById(id)
                .map(course -> {
                    courseRepository.delete(course);
                    log.info("Exiting deleteCourse method after deleting course with id: {}", id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> {
                    log.warn("Course with id: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Exiting deleteCourse method with response: {}", response);
        return response;
    }

    private void updateFields(Course existingCourse, Course courseDetails) {
        log.info("Entering updateFields method with existingCourse: {} and courseDetails: {}", existingCourse,
                courseDetails);
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
                log.error("Error updating field: {}", field.getName(), e);
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
        log.info("Exiting updateFields method");
    }

    @Operation(summary = "Search for the courses based on a field and search term")
    @PostMapping("/search")
    public List<Course> searchCourses(@RequestBody Map<String, String> searchParams) {
        log.info("Entering searchCourses method with searchParams: {}", searchParams);
        String field = searchParams.get("field");
        String searchTerm = searchParams.get("searchTerm");

        Specification<Course> spec;
        if (field != null && !field.isEmpty()) {
            spec = (root, query, cb) -> cb.like(cb.lower(root.get(field)),
                    "%" + searchTerm.toLowerCase() + "%");
        } else {
            spec = (root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("course_name")), "%" + searchTerm.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("intro")), "%" + searchTerm.toLowerCase() + "%"));
        }

        List<Course> courses = courseRepository.findAll(spec);
        log.info("Exiting searchCourses method with courses: {}", courses);
        return courses;
    }
}
