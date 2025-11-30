package pasteleria.pasteleria_backend.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pasteleria.pasteleria_backend.model.Mensaje;
import pasteleria.pasteleria_backend.repository.MensajeRepository;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    public List<Mensaje> getAllMensajes() {
        // Opcional: Podríamos ordenarlos por ID descendente para ver los nuevos primero
        return mensajeRepository.findAll();
    }

    public Mensaje saveMensaje(Mensaje mensaje) {
        // 1. GENERACIÓN AUTOMÁTICA DE FECHA
        // Si no viene fecha, ponemos la del servidor "Ahora mismo"
        if (mensaje.getFecha() == null || mensaje.getFecha().isEmpty()) {
            String fechaActual = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            mensaje.setFecha(fechaActual);
        }

        // 2. Estado inicial por defecto
        // Aseguramos que empiece como "No leído"
        mensaje.setLeido(false);

        return mensajeRepository.save(mensaje);
    }

    public Mensaje marcarComoLeido(Long id) {
        return mensajeRepository.findById(id).map(m -> {
            m.setLeido(true);
            return mensajeRepository.save(m);
        }).orElse(null);
    }

    public void deleteMensaje(Long id) {
        mensajeRepository.deleteById(id);
    }
}