package pasteleria.pasteleria_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pasteleria.pasteleria_backend.model.Mensaje;
import pasteleria.pasteleria_backend.service.MensajeService;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    @Autowired
    private MensajeService mensajeService;

    // GET: Ver todos (Admin)
    @GetMapping
    public List<Mensaje> getAll() {
        return mensajeService.getAllMensajes();
    }

    // POST: Enviar mensaje (Público - Contacto.tsx)
    @PostMapping
    public Mensaje create(@RequestBody Mensaje mensaje) {
        return mensajeService.saveMensaje(mensaje);
    }

    // PUT: Marcar como leído (Admin)
    @PutMapping("/{id}/leido")
    public Mensaje markAsRead(@PathVariable Long id) {
        return mensajeService.marcarComoLeido(id);
    }

    // DELETE: Borrar (Admin)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        mensajeService.deleteMensaje(id);
    }
}