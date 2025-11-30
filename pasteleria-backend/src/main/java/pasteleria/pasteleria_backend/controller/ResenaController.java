package pasteleria.pasteleria_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pasteleria.pasteleria_backend.model.Resena;
import pasteleria.pasteleria_backend.repository.ResenaRepository;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaRepository resenaRepository;

    @GetMapping
    public List<Resena> getAllResenas() {
        return resenaRepository.findAll();
    }

    // Endpoint crítico para la vista de Producto
    @GetMapping("/producto/{codigoProducto}")
    public List<Resena> getResenasByProducto(@PathVariable String codigoProducto) {
        return resenaRepository.findByCodigoProducto(codigoProducto);
    }

    @PostMapping
    public ResponseEntity<Resena> createResena(@RequestBody Resena resena) {
        // Asignamos la fecha del servidor para consistencia
        resena.setFecha(LocalDate.now().toString());
        Resena nueva = resenaRepository.save(resena);
        return ResponseEntity.ok(nueva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResena(@PathVariable Long id) {
        if (resenaRepository.existsById(id)) {
            resenaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}