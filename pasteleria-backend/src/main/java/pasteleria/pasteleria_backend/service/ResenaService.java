package pasteleria.pasteleria_backend.service;

import pasteleria.pasteleria_backend.model.Resena;
import pasteleria.pasteleria_backend.repository.ResenaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    public List<Resena> getAllResenas() {
        return resenaRepository.findAll();
    }

    public List<Resena> getResenasByProducto(String codigoProducto) {
        return resenaRepository.findByCodigoProducto(codigoProducto);
    }

    public Resena saveResena(Resena resena) {
        return resenaRepository.save(resena);
    }

    public void deleteResena(Long id) {
        resenaRepository.deleteById(id);
    }
}