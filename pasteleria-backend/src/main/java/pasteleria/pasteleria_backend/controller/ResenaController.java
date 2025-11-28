package pasteleria.pasteleria_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pasteleria.pasteleria_backend.model.Resena;
import pasteleria.pasteleria_backend.service.ResenaService;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    // GET: Ver todas (Para el Admin)
    @GetMapping
    public List<Resena> getAllResenas() {
        return resenaService.getAllResenas();
    }

    // GET: Ver reseñas de un producto específico (Público)
    // Ejemplo: /api/resenas/producto/TC001
    @GetMapping("/producto/{codigoProducto}")
    public List<Resena> getResenasPorProducto(@PathVariable String codigoProducto) {
        return resenaService.getResenasByProducto(codigoProducto);
    }

    // POST: Cliente escribe una reseña
    @PostMapping
    public Resena createResena(@RequestBody Resena resena) {
        return resenaService.saveResena(resena);
    }

    // DELETE: Admin borra una reseña
    @DeleteMapping("/{id}")
    public void deleteResena(@PathVariable Long id) {
        resenaService.deleteResena(id);
    }
}