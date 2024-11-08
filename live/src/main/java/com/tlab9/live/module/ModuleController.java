package com.tlab9.live.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/module")
@Tag(name = "Module", description = "API for managing modules")
@Slf4j
public class ModuleController {

    @Autowired
    private ModuleRepository moduleRepository;

    @Operation(summary = "Get all modules")
    @GetMapping
    public List<Module> getAllModules() {
        log.info("Entering getAllModules method");
        List<Module> modules = null;
        try {
            modules = moduleRepository.findAll();
            log.info("Successfully retrieved all modules");
        } catch (Exception e) {
            log.error("Error occurred while retrieving modules: ", e);
        }
        log.info("Exiting getAllModules method");
        return modules;
    }

    @Operation(summary = "Get a module by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long id) {
        log.info("Entering getModuleById method with id: {}", id);
        ResponseEntity<Module> response = null;
        try {
            response = moduleRepository.findById(id)
                    .map(module -> {
                        log.info("Module found: {}", module);
                        return ResponseEntity.ok(module);
                    })
                    .orElseGet(() -> {
                        log.warn("Module with id {} not found", id);
                        return ResponseEntity.notFound().build();
                    });
            log.info("Successfully retrieved module with id: {}", id);
        } catch (Exception e) {
            log.error("Error occurred while retrieving module with id: {}", id, e);
        }
        log.info("Exiting getModuleById method");
        return response;
    }

    @Operation(summary = "Create a new module")
    @PostMapping
    public Module createModule(@RequestBody Module module) {
        log.info("Entering createModule method with module: {}", module);
        Module createdModule = null;
        try {
            createdModule = moduleRepository.save(module);
            log.info("Successfully created module: {}", createdModule);
        } catch (Exception e) {
            log.error("Error occurred while creating module: ", e);
        }
        log.info("Exiting createModule method");
        return createdModule;
    }

    @Operation(summary = "Update a module by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Module> updateModule(@PathVariable Long id, @RequestBody Module moduleDetails) {
        log.info("Entering updateModule method with id: {} and moduleDetails: {}", id, moduleDetails);
        ResponseEntity<Module> response = moduleRepository.findById(id)
                .map(existingModule -> {
                    log.info("Module found with id: {}", id);
                    updateFields(existingModule, moduleDetails);
                    Module updatedModule = moduleRepository.save(existingModule);
                    log.info("Successfully updated module: {}", updatedModule);
                    return ResponseEntity.ok(updatedModule);
                })
                .orElseGet(() -> {
                    log.warn("Module with id {} not found", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Exiting updateModule method");
        return response;
    }

    @Operation(summary = "Delete a module by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        log.info("Entering deleteModule method with id: {}", id);
        ResponseEntity<Void> response = moduleRepository.findById(id)
                .map(module -> {
                    log.info("Module found with id: {}", id);
                    moduleRepository.delete(module);
                    log.info("Successfully deleted module with id: {}", id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> {
                    log.warn("Module with id {} not found", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Exiting deleteModule method");
        return response;
    }

    private void updateFields(Module existingModule, Module moduleDetails) {
        log.info("Entering updateFields method");
        Field[] fields = Module.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("module_id")) { // Skip the primary key field
                    Object newValue = field.get(moduleDetails);
                    if (newValue != null) {
                        log.info("Updating field: {} with new value: {}", field.getName(), newValue);
                        field.set(existingModule, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("Failed to update field: {}", field.getName(), e);
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
        log.info("Exiting updateFields method");
    }

    @Operation(summary = "Search for the modules based on a field and search term")
    @PostMapping("/search")
    public List<Module> searchModules(@RequestBody Map<String, String> searchParams) {
        log.info("Entering searchModules method with searchParams: {}", searchParams);
        String field = searchParams.get("field");
        String searchTerm = searchParams.get("searchTerm");
    
        Specification<Module> spec;
        if (field != null && !field.isEmpty()) {
            log.info("Searching modules by field: {} with searchTerm: {}", field, searchTerm);
            spec = (root, query, cb) -> cb.like(cb.lower(root.get(field)),
                    "%" + searchTerm.toLowerCase() + "%");
        } else {
            log.info("Searching modules by default fields with searchTerm: {}", searchTerm);
            spec = (root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("module_name")), "%" + searchTerm.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("intro")), "%" + searchTerm.toLowerCase() + "%"));
        }
    
        List<Module> modules = moduleRepository.findAll(spec);
        log.info("Successfully found {} modules", modules.size());
        log.info("Exiting searchModules method");
        return modules;
    }

    @Operation(summary = "Get modules by subject ID")
    @GetMapping("/bysubjectid/{subject_id}")
    public List<Module> getModulesBySubjectId(@PathVariable("subject_id") String subjectId) {
        log.info("Entering getModulesBySubjectId method with subject_id: {}", subjectId);

        Specification<Module> spec = (root, query, cb) -> cb.equal(root.get("subject_id"), subjectId);

        List<Module> modules = moduleRepository.findAll(spec);
        log.info("Exiting getModulesBySubjectId method with modules: {}", modules);

        return modules;
    }

    @Operation(summary = "Update modules list in the subject")
    @PostMapping("/updateseqno")
    public ResponseEntity<String> updateSequenceNumbers(@RequestBody List<Module> modules) {
        log.info("Entering updateSequenceNumbers method with modules: {}", modules);

        for (Module module : modules) {
            Module existingModule = moduleRepository.findById(module.getModule_id())
                    .orElseGet(() -> {
                        log.warn("Module not found with id " + module.getModule_id());
                        return null;
                    });

            if (existingModule == null) {
                String errorMessage = "Module not found with id " + module.getModule_id();
                log.error(errorMessage);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }

            existingModule.setSeq_no(module.getSeq_no());
            moduleRepository.save(existingModule);
        }

        log.info("Exiting updateSequenceNumbers method");
        return ResponseEntity.ok("Sequence numbers updated successfully");
    }

}