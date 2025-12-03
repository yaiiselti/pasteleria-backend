package pasteleria.pasteleria_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pasteleria.pasteleria_backend.model.Producto;
import pasteleria.pasteleria_backend.model.Resena;
import pasteleria.pasteleria_backend.repository.ProductoRepository; // <--- IMPORTANTE
import pasteleria.pasteleria_backend.repository.ResenaRepository;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private ProductoRepository productoRepository; 

    public List<Resena> getAllResenas() {
        return resenaRepository.findAll();
    }

    public List<Resena> getResenasByProducto(String codigoProducto) {
        return resenaRepository.findByCodigoProducto(codigoProducto);
    }

    // @Transactional: Si algo falla aquí, no se guarda NADA en la BD (Rollback)
    @Transactional 
    public Resena saveResena(Resena resena) {
        // 1. Validar que el producto exista
        Producto producto = productoRepository.findById(resena.getCodigoProducto()).orElse(null);
        
        if (producto == null || (producto.getActivo() != null && !producto.getActivo())) {
            throw new RuntimeException("Error: El producto no existe o no está disponible.");
        }

        // 2. Validar Spam (Un usuario = Una reseña por producto)
        List<Resena> existentes = resenaRepository.findByCodigoProducto(resena.getCodigoProducto());
        boolean yaOpino = existentes.stream()
            .anyMatch(r -> r.getEmailUsuario().equalsIgnoreCase(resena.getEmailUsuario()));

        if (yaOpino) {
            throw new RuntimeException("Error: Ya opinaste sobre este producto.");
        }

        return resenaRepository.save(resena);
    }

    @Transactional
    public void deleteResena(Long id) {
        resenaRepository.deleteById(id);
    }
}