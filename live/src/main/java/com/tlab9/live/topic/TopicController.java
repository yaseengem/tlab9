package com.tlab9.live.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @GetMapping
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable Long id) {
        return topicRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Topic createTopic(@RequestBody Topic topic) {
        return topicRepository.save(topic);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @RequestBody Topic topicDetails) {
        return topicRepository.findById(id)
                .map(existingTopic -> {
                    existingTopic.setTopic_name(topicDetails.getTopic_name());
                    existingTopic.setTopic_type(topicDetails.getTopic_type());
                    existingTopic.setContent(topicDetails.getContent());
                    existingTopic.setSequence_no(topicDetails.getSequence_no());
                    Topic updatedTopic = topicRepository.save(existingTopic);
                    return ResponseEntity.ok(updatedTopic);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        return topicRepository.findById(id)
                .map(topic -> {
                    topicRepository.delete(topic);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}