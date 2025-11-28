package pasteleria.pasteleria_backend.service;

import pasteleria.pasteleria_backend.model.Producto;
import pasteleria.pasteleria_backend.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todos
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    // Obtener por código
    public Optional<Producto> getProductoByCodigo(String codigo) {
        return productoRepository.findById(codigo);
    }

    // Guardar o Actualizar
    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // Eliminar
    public void deleteProducto(String codigo) {
        productoRepository.deleteById(codigo);
    }
}