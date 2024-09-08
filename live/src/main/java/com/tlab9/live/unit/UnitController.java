package com.tlab9.live.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                    existingUnit.setUnit_name(unitDetails.getUnit_name());
                    existingUnit.setDescription(unitDetails.getDescription());
                    existingUnit.setSequence_no(unitDetails.getSequence_no());
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
}