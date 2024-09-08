package com.tlab9.live.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        return courseRepository.findById(id)
                .map(existingCourse -> {
                    existingCourse.setCourse_name(courseDetails.getCourse_name());
                    existingCourse.setDescription(courseDetails.getDescription());
                    existingCourse.setCreated_by(courseDetails.getCreated_by());
                    existingCourse.setIs_active(courseDetails.isIs_active());
                    existingCourse.setHead_video_url(courseDetails.getHead_video_url());
                    existingCourse.setHead_image_url(courseDetails.getHead_image_url());
                    Course updatedCourse = courseRepository.save(existingCourse);
                    return ResponseEntity.ok(updatedCourse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setIs_deleted(true);
                    courseRepository.save(course);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}