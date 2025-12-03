package pasteleria.pasteleria_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // Excepción de bloqueo
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pasteleria.pasteleria_backend.model.Usuario;
import pasteleria.pasteleria_backend.repository.UsuarioRepository;
import pasteleria.pasteleria_backend.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{run}")
    public ResponseEntity<Usuario> getUsuarioByRun(@PathVariable String run) {
        return usuarioRepository.findById(run)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveUsuario(@RequestBody Usuario usuario) {
        try {
            if (usuario.getTipo() == null || usuario.getTipo().isEmpty()) {
                usuario.setTipo("Cliente");
            }
            
            Usuario guardado = usuarioService.saveUsuario(usuario);
            return ResponseEntity.ok(guardado);

        } catch (OptimisticLockingFailureException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Error: El usuario fue modificado por otro administrador mientras editabas. Recarga la página.");
        }
    }

    @DeleteMapping("/{run}")
    public ResponseEntity<?> deleteUsuario(@PathVariable String run) {
        if (usuarioRepository.existsById(run)) {
            usuarioRepository.deleteById(run);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}