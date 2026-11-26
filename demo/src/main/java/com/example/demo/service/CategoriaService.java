package com.example.demo.service;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.CategoriaRequest;
import com.example.demo.dto.CategoriaResponse;
import com.example.demo.model.Department;
import com.example.demo.model.Producto;
import com.example.demo.repo.categoriaRepo;
import com.example.demo.repo.productoRepo;

@Service
public class CategoriaService {

    private final categoriaRepo repo;
    private final productoRepo productoRepository;

    public CategoriaService(categoriaRepo repo, productoRepo productoRepository) {
        this.repo = repo;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> findAll() {
        return repo.findAll().stream()
                .map(CategoriaResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse findById(String id) {
        Department categoria = getEntityById(id);
        return CategoriaResponse.from(categoria);
    }

    @Transactional
    public CategoriaResponse create(CategoriaRequest request) {
        Department saved = repo.save(request.toEntity());
        return CategoriaResponse.from(saved);
    }

    @Transactional
    public CategoriaResponse update(String id, CategoriaRequest request) {
        Department categoria = getEntityById(id);
        request.updateEntity(categoria);
        Department updated = repo.save(categoria);
        return CategoriaResponse.from(updated);
    }

    @Transactional
    public void delete(String id) {
        Department categoria = getEntityById(id);
        repo.delete(categoria);
    }

    @Transactional
    public CategoriaResponse addProductoToCategoria(String productId, String categoriaId) {
        Producto producto = productoRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado con id: " + productId));
        Department categoria = getEntityById(categoriaId);
        categoria.getProductos().add(producto);
        Department updated = repo.save(categoria);
        return CategoriaResponse.from(updated);
    }

    @Transactional(readOnly = true)
    public Set<Department> getCategoriasByProducto(String productId) {
        Producto producto = productoRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado con id: " + productId));
        return producto.getDeparments();
    }

    @Transactional(readOnly = true)
    public Department getEntityById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoría no encontrada con id: " + id));
    }
}