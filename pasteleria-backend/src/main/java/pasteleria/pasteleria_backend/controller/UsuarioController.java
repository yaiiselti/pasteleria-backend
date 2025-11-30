package pasteleria.pasteleria_backend.controller;

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
    public ResponseEntity<Usuario> saveUsuario(@RequestBody Usuario usuario) {
        // Validaciones básicas de integridad
        if (usuario.getTipo() == null || usuario.getTipo().isEmpty()) {
            usuario.setTipo("Cliente");
        }
        
        // El servicio se encarga de no re-encriptar la contraseña si ya es un hash
        // Esto permite editar otros campos sin romper el login
        Usuario guardado = usuarioService.saveUsuario(usuario);
        return ResponseEntity.ok(guardado);
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