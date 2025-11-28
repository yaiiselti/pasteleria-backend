package pasteleria.pasteleria_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // <--- Usamos el Servicio
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pasteleria.pasteleria_backend.model.Producto;
import pasteleria.pasteleria_backend.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService; // <--- Inyección del Servicio

    // GET: Público
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    // GET: Público
    @GetMapping("/{codigo}")
    public ResponseEntity<Producto> getProducto(@PathVariable String codigo) {
        Optional<Producto> producto = productoService.getProductoByCodigo(codigo);
        return producto.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: Solo Admin
    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.saveProducto(producto);
    }
    
    // DELETE: Solo Admin
    @DeleteMapping("/{codigo}")
    public void deleteProducto(@PathVariable String codigo) {
        productoService.deleteProducto(codigo);
    }
}