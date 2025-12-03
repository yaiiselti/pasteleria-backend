package pasteleria.pasteleria_backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; // Usamos el Servicio mejorado
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pasteleria.pasteleria_backend.model.Resena;
import pasteleria.pasteleria_backend.service.ResenaService;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService; // Usamos Service, no Repository directo

    @GetMapping
    public List<Resena> getAllResenas() {
        return resenaService.getAllResenas();
    }

    @GetMapping("/producto/{codigoProducto}")
    public List<Resena> getResenasByProducto(@PathVariable String codigoProducto) {
        return resenaService.getResenasByProducto(codigoProducto);
    }

    @PostMapping
    public ResponseEntity<?> createResena(@RequestBody Resena resena) {
        try {
            // Asignamos fecha servidor
            resena.setFecha(LocalDate.now().toString());
            
            // Intentamos guardar (El servicio hará las validaciones)
            Resena nueva = resenaService.saveResena(resena);
            return ResponseEntity.ok(nueva);
            
        } catch (RuntimeException e) {
            // Capturamos el error de validación (Spam o Producto Fantasma)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResena(@PathVariable Long id) {
        resenaService.deleteResena(id);
        return ResponseEntity.ok().build();
    }
}