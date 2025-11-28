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

import pasteleria.pasteleria_backend.model.Usuario;
import pasteleria.pasteleria_backend.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET: Listar todos (Para el AdminDashboard)
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    // GET: Buscar uno por RUN (Para editar)
    @GetMapping("/{run}")
    public Usuario getUsuario(@PathVariable String run) {
        return usuarioService.getUsuarioByRun(run).orElse(null);
    }

    // POST: El Admin crea un usuario manualmente (además del Registro público)
    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.saveUsuario(usuario);
    }

    // DELETE: Eliminar usuario
    @DeleteMapping("/{run}")
    public void deleteUsuario(@PathVariable String run) {
        usuarioService.deleteUsuario(run);
    }
}