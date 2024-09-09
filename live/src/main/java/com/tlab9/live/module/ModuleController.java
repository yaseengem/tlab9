package com.tlab9.live.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    @Autowired
    private ModuleRepository moduleRepository;

    @GetMapping
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long id) {
        return moduleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Module createModule(@RequestBody Module module) {
        return moduleRepository.save(module);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Module> updateModule(@PathVariable Long id, @RequestBody Module moduleDetails) {
        return moduleRepository.findById(id)
                .map(existingModule -> {
                    updateFields(existingModule, moduleDetails);
                    Module updatedModule = moduleRepository.save(existingModule);
                    return ResponseEntity.ok(updatedModule);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        return moduleRepository.findById(id)
                .map(module -> {
                    moduleRepository.delete(module);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private void updateFields(Module existingModule, Module moduleDetails) {
        Field[] fields = Module.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("module_id")) { // Skip the primary key field
                    Object newValue = field.get(moduleDetails);
                    if (newValue != null) {
                        field.set(existingModule, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
    }

    @PostMapping("/search")
    public List<Module> searchModules(@RequestBody Map<String, String> searchParams) {
        String columnName = searchParams.get("columnName");
        String searchTerm = searchParams.get("searchTerm");

        Specification<Module> spec = (root, query, cb) -> cb.like(cb.lower(root.get(columnName)), "%" + searchTerm.toLowerCase() + "%");

        return moduleRepository.findAll(spec);
    }
}