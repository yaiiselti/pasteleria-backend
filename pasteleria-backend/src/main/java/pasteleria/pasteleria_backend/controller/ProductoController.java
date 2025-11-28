package pasteleria.pasteleria_backend.controller;
import pasteleria.pasteleria_backend.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import pasteleria.pasteleria_backend.repository.ProductoRepository;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // GET http://localhost:8085/api/productos
    // Público: Cualquiera puede ver la lista
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    // GET http://localhost:8085/api/productos/{codigo}
    @GetMapping("/{codigo}")
    public Producto getProducto(@PathVariable String codigo) {
        return productoRepository.findById(codigo).orElse(null);
    }

    // POST http://localhost:8085/api/productos
    // Privado: Solo Admin (Spring Security lo protege por configuración o anotación)
    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }
    
    // DELETE http://localhost:8085/api/productos/{codigo}
    @DeleteMapping("/{codigo}")
    public void deleteProducto(@PathVariable String codigo) {
        productoRepository.deleteById(codigo);
    }
}