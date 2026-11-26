package com.example.demo.controllers;

import java.util.List;

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
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ImagenRequest;
import com.example.demo.dto.ImagenResponse;
import com.example.demo.model.Image;
import com.example.demo.model.Producto;
import com.example.demo.repo.imagenRepo;
import com.example.demo.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/imagenes")
public class ImagenController {

    private final imagenRepo repo;
    private final ProductoService productoService;

    public ImagenController(imagenRepo repo, ProductoService productoService) {
        this.repo = repo;
        this.productoService = productoService;
    }

    @GetMapping
    public List<ImagenResponse> findAll() {
        return repo.findAll().stream()
                .map(ImagenResponse::from)
                .toList();
    }

    @GetMapping("/producto/{productId}")
    public List<ImagenResponse> findByProducto(@PathVariable String productId) {
        productoService.getEntityById(productId);
        return repo.findAll().stream()
                .filter(img -> img.getProducto() != null && productId.equals(img.getProducto().getId()))
                .map(ImagenResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ImagenResponse findById(@PathVariable String id) {
        Image imagen = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Imagen no encontrada con id: " + id));
        return ImagenResponse.from(imagen);
    }

    @PostMapping("/producto/{productId}")
    public ResponseEntity<ImagenResponse> create(
            @PathVariable String productId,
            @Valid @RequestBody ImagenRequest request) {
        Producto producto = productoService.getEntityById(productId);
        Image imagen = new Image();
        imagen.setUrl(request.url());
        imagen.setPrioridad(request.prioridad());
        imagen.setTamanio(request.tamanio());
        imagen.setResponsiva(request.responsiva());
        imagen.setProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ImagenResponse.from(repo.save(imagen)));
    }

    @PutMapping("/{id}")
    public ImagenResponse update(
            @PathVariable String id,
            @Valid @RequestBody ImagenRequest request) {
        Image imagen = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Imagen no encontrada con id: " + id));
        imagen.setUrl(request.url());
        imagen.setPrioridad(request.prioridad());
        imagen.setTamanio(request.tamanio());
        imagen.setResponsiva(request.responsiva());
        return ImagenResponse.from(repo.save(imagen));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        Image imagen = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Imagen no encontrada con id: " + id));
        repo.delete(imagen);
        return ResponseEntity.noContent().build();
    }
}