package com.example.demo.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CategoriaRequest;
import com.example.demo.dto.CategoriaResponse;
import com.example.demo.model.Department;
import com.example.demo.service.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CategoriaResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> create(@Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public CategoriaResponse update(@PathVariable String id, @Valid @RequestBody CategoriaRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/producto/{productId}/categoria/{categoriaId}")
    public ResponseEntity<CategoriaResponse> addProductoToCategoria(
            @PathVariable String productId,
            @PathVariable String categoriaId) {
        CategoriaResponse updated = service.addProductoToCategoria(productId, categoriaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @GetMapping("/producto/{productId}")
    public Set<Department> getCategoriasByProducto(@PathVariable String productId) {
        return service.getCategoriasByProducto(productId);
    }
}