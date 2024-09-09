package com.tlab9.live.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/units")
public class UnitController {

    @Autowired
    private UnitRepository unitRepository;

    @GetMapping
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unit> getUnitById(@PathVariable Long id) {
        return unitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Unit createUnit(@RequestBody Unit unit) {
        return unitRepository.save(unit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Unit> updateUnit(@PathVariable Long id, @RequestBody Unit unitDetails) {
        return unitRepository.findById(id)
                .map(existingUnit -> {
                    updateFields(existingUnit, unitDetails);
                    Unit updatedUnit = unitRepository.save(existingUnit);
                    return ResponseEntity.ok(updatedUnit);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        return unitRepository.findById(id)
                .map(unit -> {
                    unitRepository.delete(unit);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private void updateFields(Unit existingUnit, Unit unitDetails) {
        Field[] fields = Unit.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (!field.getName().equals("unit_id")) { // Skip the primary key field
                    Object newValue = field.get(unitDetails);
                    if (newValue != null) {
                        field.set(existingUnit, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }
    }

    @PostMapping("/search")
    public List<Unit> searchUnits(@RequestBody Map<String, String> searchParams) {
        String columnName = searchParams.get("columnName");
        String searchTerm = searchParams.get("searchTerm");

        Specification<Unit> spec = (root, query, cb) -> cb.like(cb.lower(root.get(columnName)), "%" + searchTerm.toLowerCase() + "%");

        return unitRepository.findAll(spec);
    }
}