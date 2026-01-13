package com.project.naturalmarket.controller;

import com.project.naturalmarket.dto.ProductDto;
import com.project.naturalmarket.dto.SupplierDto;
import com.project.naturalmarket.entity.Product;
import com.project.naturalmarket.service.SupplierService;
import com.project.naturalmarket.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/supplier")
@RestController
public class SupplierController {

    private SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierDto> create(@Valid @RequestBody SupplierDto supplierDto){

        SupplierDto supplierDto1=supplierService.create(supplierDto);

        return new ResponseEntity<>(supplierDto1, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<SupplierDto>> getAll(){

        return new ResponseEntity<>(supplierService.getSuppliers(),HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getById(@PathVariable(name = "id") long id){

        return new ResponseEntity<>(supplierService.getById(id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> update(@Valid @RequestBody SupplierDto supplierDto,
                                              @PathVariable(name = "id") long id){

        return new ResponseEntity<>(supplierService.update(supplierDto,id),HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") long id){

        supplierService.delete(id);

        return ResponseEntity.ok("Se elimino Correctamente");
    }
}
