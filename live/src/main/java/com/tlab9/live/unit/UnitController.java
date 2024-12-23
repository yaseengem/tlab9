package com.tlab9.live.unit;

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
@RequestMapping("/api/unit")
@Slf4j
public class UnitController {

    @Autowired
    private UnitRepository unitRepository;

    @Operation(summary = "Get all units")
    @GetMapping
    public List<Unit> getAllUnits() {
        log.info("Entering getAllUnits method");
        List<Unit> units = unitRepository.findAll();
        log.info("Successfully found {} units", units.size());
        log.info("Exiting getAllUnits method");
        return units;
    }

    @Operation(summary = "Get a unit by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Unit> getUnitById(@PathVariable Long id) {
        log.info("Entering getUnitById method with id: {}", id);
        ResponseEntity<Unit> response = unitRepository.findById(id)
                .map(unit -> {
                    log.info("Unit found with id: {}", id);
                    return ResponseEntity.ok(unit);
                })
                .orElseGet(() -> {
                    log.warn("Unit with id {} not found", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Exiting getUnitById method with response: {}", response);
        return response;
    }

    @Operation(summary = "Create a new unit")
    @PostMapping
    public Unit createUnit(@RequestBody Unit unit) {
        log.info("Entering createUnit method with unit: {}", unit);
        Unit createdUnit = unitRepository.save(unit);
        log.info("Successfully created unit: {}", createdUnit);
        log.info("Exiting createUnit method");
        return createdUnit;
    }

    @Operation(summary = "Update a unit by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Unit> updateUnit(@PathVariable Long id, @RequestBody Unit unitDetails) {
        log.info("Entering updateUnit method with id: {} and unitDetails: {}", id, unitDetails);
        ResponseEntity<Unit> response = unitRepository.findById(id)
                .map(existingUnit -> {
                    log.info("Unit found with id: {}", id);
                    updateFields(existingUnit, unitDetails);
                    Unit updatedUnit = unitRepository.save(existingUnit);
                    log.info("Successfully updated unit: {}", updatedUnit);
                    return ResponseEntity.ok(updatedUnit);
                })
                .orElseGet(() -> {
                    log.warn("Unit with id {} not found", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Exiting updateUnit method with response: {}", response);
        return response;
    }

    @Operation(summary = "Delete a unit by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        log.info("Entering deleteUnit method with id: {}", id);
        ResponseEntity<Void> response = unitRepository.findById(id)
                .map(unit -> {
                    unitRepository.delete(unit);
                    log.info("Exiting deleteUnit method after deleting unit with id: {}", id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> {
                    log.warn("Unit with id: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Exiting deleteUnit method with response: {}", response);
        return response;
    }

    private void updateFields(Unit existingUnit, Unit unitDetails) {
        log.info("Entering updateFields method");
        Field[] fields = Unit.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("unit_id")) { // Skip the primary key field
                    Object newValue = field.get(unitDetails);
                    if (newValue != null) {
                        log.info("Updating field: {} with new value: {}", field.getName(), newValue);
                        field.set(existingUnit, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("Failed to update field: {}", field.getName(), e);
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
        log.info("Exiting updateFields method");
    }

    @Operation(summary = "Search for the units based on a field and search term")
    @PostMapping("/search")
    public List<Unit> searchUnits(@RequestBody Map<String, String> searchParams) {
        log.info("Entering searchUnits method with searchParams: {}", searchParams);
        String field = searchParams.get("field");
        String searchTerm = searchParams.get("searchTerm");

        Specification<Unit> spec;
        if (field != null && !field.isEmpty()) {
            log.info("Searching units by field: {} with searchTerm: {}", field, searchTerm);
            spec = (root, query, cb) -> cb.like(cb.lower(root.get(field)),
                    "%" + searchTerm.toLowerCase() + "%");
        } else {
            log.info("Searching units by default fields with searchTerm: {}", searchTerm);
            spec = (root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("unit_name")), "%" + searchTerm.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("intro")), "%" + searchTerm.toLowerCase() + "%"));
        }
        List<Unit> units = unitRepository.findAll(spec);
        log.info("Successfully found {} units", units.size());
        log.info("Exiting searchUnits method");
        return units;
    }

    @Operation(summary = "Get modules by unit ID")
    @GetMapping("/bymoduleid/{module_id}")
    public List<Unit> getModulesByUnitId(@PathVariable("module_id") String moduleId) {
        log.info("Entering getModulesByUnitId method with module_id: {}", moduleId);

        Specification<Unit> spec = (root, query, cb) -> cb.equal(root.get("module_id"), moduleId);

        List<Unit> units = unitRepository.findAll(spec);
        log.info("Exiting getModulesByUnitId method with units: {}", units);

        return units;
    }

    @Operation(summary = "Update modules list in the unit")
    @PostMapping("/updateseqno")
    public ResponseEntity<String> updateSequenceNumbers(@RequestBody List<Unit> units) {
        log.info("Entering updateSequenceNumbers method with units: {}", units);

        for (Unit unit : units) {
            Unit existingUnit = unitRepository.findById(unit.getUnit_id())
                    .orElseGet(() -> {
                        log.warn("Unit not found with id " + unit.getUnit_id());
                        return null;
                    });

            if (existingUnit == null) {
                String errorMessage = "Unit not found with id " + unit.getUnit_id();
                log.error(errorMessage);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }

            existingUnit.setSeq_no(unit.getSeq_no());
            unitRepository.save(existingUnit);
        }

        log.info("Exiting updateSequenceNumbers method");
        return ResponseEntity.ok("Sequence numbers updated successfully");
    }
}
