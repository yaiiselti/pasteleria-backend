package pasteleria.pasteleria_backend.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pasteleria.pasteleria_backend.model.Mensaje;
import pasteleria.pasteleria_backend.repository.MensajeRepository;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    @Autowired
    private MensajeRepository mensajeRepository;

    @GetMapping
    public List<Mensaje> getAllMensajes() {
        // Podríamos ordenar por fecha descendente
        return mensajeRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Mensaje> createMensaje(@RequestBody Mensaje mensaje) {
        // Asignamos fecha servidor si viene vacía
        if (mensaje.getFecha() == null) {
            mensaje.setFecha(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        mensaje.setLeido(false); // Por defecto no leído
        return ResponseEntity.ok(mensajeRepository.save(mensaje));
    }

    // CAMBIO: Endpoint para marcar como leído
    @PutMapping("/{id}/leido")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        return mensajeRepository.findById(id).map(mensaje -> {
            mensaje.setLeido(true);
            mensajeRepository.save(mensaje);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMensaje(@PathVariable Long id) {
        if (mensajeRepository.existsById(id)) {
            mensajeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}