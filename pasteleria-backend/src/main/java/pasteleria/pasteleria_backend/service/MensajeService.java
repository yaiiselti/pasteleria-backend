package pasteleria.pasteleria_backend.service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        return mensajeRepository.findAll();
    }

    public Mensaje saveMensaje(Mensaje mensaje) {
        // Si es nuevo, ponemos la fecha de hoy y leido=false automáticamente
        if (mensaje.getId() == null) {
            mensaje.setLeido(false);
            if (mensaje.getFecha() == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                mensaje.setFecha(sdf.format(new Date()));
            }
        }
        return mensajeRepository.save(mensaje);
    }

    public void deleteMensaje(Long id) {
        mensajeRepository.deleteById(id);
    }

    public Mensaje marcarComoLeido(Long id) {
        return mensajeRepository.findById(id).map(m -> {
            m.setLeido(true);
            return mensajeRepository.save(m);
        }).orElse(null);
    }
}