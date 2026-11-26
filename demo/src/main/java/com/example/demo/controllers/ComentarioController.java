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

import com.example.demo.dto.ComentarioRequest;
import com.example.demo.dto.ComentarioResponse;
import com.example.demo.model.Comment;
import com.example.demo.model.Producto;
import com.example.demo.repo.comentarioRepo;
import com.example.demo.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final comentarioRepo repo;
    private final ProductoService productoService;

    public ComentarioController(comentarioRepo repo, ProductoService productoService) {
        this.repo = repo;
        this.productoService = productoService;
    }

    @GetMapping
    public List<ComentarioResponse> findAll() {
        return repo.findAll().stream()
                .map(ComentarioResponse::from)
                .toList();
    }

    @GetMapping("/producto/{productId}")
    public List<ComentarioResponse> findByProducto(@PathVariable String productId) {
        productoService.getEntityById(productId);
        return repo.findAll().stream()
                .filter(c -> c.getProducto() != null && productId.equals(c.getProducto().getId()))
                .map(ComentarioResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ComentarioResponse findById(@PathVariable String id) {
        Comment comentario = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Comentario no encontrado con id: " + id));
        return ComentarioResponse.from(comentario);
    }

    @PostMapping("/producto/{productId}")
    public ResponseEntity<ComentarioResponse> create(
            @PathVariable String productId,
            @Valid @RequestBody ComentarioRequest request) {
        Producto producto = productoService.getEntityById(productId);
        Comment comentario = new Comment();
        comentario.setUsername(request.username());
        comentario.setComentario_corto(request.comentario_corto());
        comentario.setEstrellas(request.estrellas());
        comentario.setComentario_largo(request.comentario_largo());
        comentario.setImagen1(request.imagen1());
        comentario.setImagen2(request.imagen2());
        comentario.setProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ComentarioResponse.from(repo.save(comentario)));
    }

    @PutMapping("/{id}")
    public ComentarioResponse update(
            @PathVariable String id,
            @Valid @RequestBody ComentarioRequest request) {
        Comment comentario = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Comentario no encontrado con id: " + id));
        comentario.setUsername(request.username());
        comentario.setComentario_corto(request.comentario_corto());
        comentario.setEstrellas(request.estrellas());
        comentario.setComentario_largo(request.comentario_largo());
        comentario.setImagen1(request.imagen1());
        comentario.setImagen2(request.imagen2());
        return ComentarioResponse.from(repo.save(comentario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        Comment comentario = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Comentario no encontrado con id: " + id));
        repo.delete(comentario);
        return ResponseEntity.noContent().build();
    }
}