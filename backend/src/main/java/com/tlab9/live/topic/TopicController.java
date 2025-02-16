package com.tlab9.live.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topic")
@Slf4j
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @Operation(summary = "Get all topics")
    @GetMapping
    public List<Topic> getAllTopics() {
        log.info("Entering getAllTopics method");
        List<Topic> topics = topicRepository.findAll();
        log.info("Successfully found {} topics", topics.size());
        log.info("Exiting getAllTopics method");
        return topics;
    }

    @Operation(summary = "Get a topic by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable Long id) {
        log.info("Entering getTopicById method with id: {}", id);
        ResponseEntity<Topic> response = topicRepository.findById(id)
                .map(topic -> {
                    log.info("Topic found with id: {}", id);
                    return ResponseEntity.ok(topic);
                })
                .orElseGet(() -> {
                    log.warn("Topic with id {} not found", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Exiting getTopicById method with response: {}", response);
        return response;
    }

    @Operation(summary = "Create a new topic")
    @PostMapping
    public Topic createTopic(@RequestBody Topic topic) {
        log.info("Entering createTopic method with topic: {}", topic);
        Topic createdTopic = topicRepository.save(topic);
        log.info("Successfully created topic: {}", createdTopic);
        log.info("Exiting createTopic method");
        return createdTopic;
    }

    @Operation(summary = "Update a topic by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @RequestBody Topic topicDetails) {
        log.info("Entering updateTopic method with id: {} and topicDetails: {}", id, topicDetails);
        ResponseEntity<Topic> response = topicRepository.findById(id)
                .map(existingTopic -> {
                    log.info("Topic found with id: {}", id);
                    updateFields(existingTopic, topicDetails);
                    Topic updatedTopic = topicRepository.save(existingTopic);
                    log.info("Successfully updated topic: {}", updatedTopic);
                    return ResponseEntity.ok(updatedTopic);
                })
                .orElseGet(() -> {
                    log.warn("Topic with id {} not found", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Exiting updateTopic method with response: {}", response);
        return response;
    }

    @Operation(summary = "Delete a topic by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        log.info("Entering deleteTopic method with id: {}", id);
        ResponseEntity<Void> response = topicRepository.findById(id)
                .map(topic -> {
                    topicRepository.delete(topic);
                    log.info("Exiting deleteTopic method after deleting topic with id: {}", id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> {
                    log.warn("Topic with id: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Exiting deleteTopic method with response: {}", response);
        return response;
    }

    private void updateFields(Topic existingTopic, Topic topicDetails) {
        log.info("Entering updateFields method");
        Field[] fields = Topic.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("topic_id")) { // Skip the primary key field
                    Object newValue = field.get(topicDetails);
                    if (newValue != null) {
                        log.info("Updating field: {} with new value: {}", field.getName(), newValue);
                        field.set(existingTopic, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("Failed to update field: {}", field.getName(), e);
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
        log.info("Exiting updateFields method");
    }

    @Operation(summary = "Search for the topics based on a field and search term")
    @PostMapping("/search")
    public List<Topic> searchTopics(@RequestBody Map<String, String> searchParams) {
        log.info("Entering searchTopics method with searchParams: {}", searchParams);
        String field = searchParams.get("field");
        String searchTerm = searchParams.get("searchTerm");

        Specification<Topic> spec;
        if (field != null && !field.isEmpty()) {
            log.info("Searching topics by field: {} with searchTerm: {}", field, searchTerm);
            spec = (root, query, cb) -> cb.like(cb.lower(root.get(field)),
                    "%" + searchTerm.toLowerCase() + "%");
        } else {
            log.info("Searching topics by default fields with searchTerm: {}", searchTerm);
            spec = (root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("topic_name")), "%" + searchTerm.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("intro")), "%" + searchTerm.toLowerCase() + "%")
            );
        }

        List<Topic> topics = topicRepository.findAll(spec);
        log.info("Successfully found {} topics", topics.size());
        log.info("Exiting searchTopics method");
        return topics;
    }

    @Operation(summary = "Get units by topic ID")
    @GetMapping("/byunitid/{unit_id}")
    public List<Topic> getUnitsByTopicId(@PathVariable("unit_id") String unitId) {
        log.info("Entering getUnitsByTopicId method with unit_id: {}", unitId);

        Specification<Topic> spec = (root, query, cb) -> cb.equal(root.get("unit_id"), unitId);

        List<Topic> topics = topicRepository.findAll(spec);
        log.info("Exiting getUnitsByTopicId method with topics: {}", topics);

        return topics;
    }

    @Operation(summary = "Update units list in the topic")
    @PostMapping("/updateseqno")
    public ResponseEntity<String> updateSequenceNumbers(@RequestBody List<Topic> topics) {
        log.info("Entering updateSequenceNumbers method with topics: {}", topics);

        for (Topic topic : topics) {
            Topic existingTopic = topicRepository.findById(topic.getTopic_id())
                    .orElseGet(() -> {
                        log.warn("Topic not found with id " + topic.getTopic_id());
                        return null;
                    });

            if (existingTopic == null) {
                String errorMessage = "Topic not found with id " + topic.getTopic_id();
                log.error(errorMessage);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }

            existingTopic.setSeq_no(topic.getSeq_no());
            topicRepository.save(existingTopic);
        }

        log.info("Exiting updateSequenceNumbers method");
        return ResponseEntity.ok("Sequence numbers updated successfully");
    }
}