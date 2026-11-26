package com.example.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.ProductoPatchRequest;
import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponse;
import com.example.demo.model.Producto;
import com.example.demo.repo.productoRepo;

@Service
public class ProductoService {

    private final productoRepo repo;

    public ProductoService(productoRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> findAll() {
        return repo.findAll().stream()
                .map(ProductoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse findById(String id) {
        Producto producto = getEntityById(id);
        return ProductoResponse.from(producto);
    }

    @Transactional
    public ProductoResponse create(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setLote(request.lote());
        producto.setPrecioUnitario(request.precioUnitario());
        producto.setFechaCaducidad(request.fechaCaducidad());
        return ProductoResponse.from(repo.save(producto));
    }

    @Transactional
    public ProductoResponse update(String id, ProductoRequest request) {
        Producto producto = getEntityById(id);
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setLote(request.lote());
        producto.setPrecioUnitario(request.precioUnitario());
        producto.setFechaCaducidad(request.fechaCaducidad());
        return ProductoResponse.from(repo.save(producto));
    }

    @Transactional
    public ProductoResponse patch(String id, ProductoPatchRequest request) {
        Producto producto = getEntityById(id);
        if (request.nombre() != null) {
            producto.setNombre(request.nombre());
        }
        if (request.descripcion() != null) {
            producto.setDescripcion(request.descripcion());
        }
        if (request.lote() != null) {
            producto.setLote(request.lote());
        }
        if (request.precioUnitario() != null) {
            producto.setPrecioUnitario(request.precioUnitario());
        }
        if (request.fechaCaducidad() != null) {
            producto.setFechaCaducidad(request.fechaCaducidad());
        }
        return ProductoResponse.from(repo.save(producto));
    }

    @Transactional
    public void delete(String id) {
        Producto producto = getEntityById(id);
        repo.delete(producto);
    }

    @Transactional(readOnly = true)
    public Producto getEntityById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado con id: " + id));
    }
}