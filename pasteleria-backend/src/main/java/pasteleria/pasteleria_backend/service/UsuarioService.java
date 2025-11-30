package pasteleria.pasteleria_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pasteleria.pasteleria_backend.model.Usuario;
import pasteleria.pasteleria_backend.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioByRun(String run) {
        return usuarioRepository.findById(run);
    }

    public Optional<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario saveUsuario(Usuario usuario) {
        // LÓGICA INTELIGENTE DE CONTRASEÑAS
        
        // 1. Verificamos si el usuario ya existe (es una EDICIÓN)
        if (usuarioRepository.existsById(usuario.getRun())) {
            Usuario usuarioAntiguo = usuarioRepository.findById(usuario.getRun()).get();

            // 2. Si la contraseña viene vacía o nula, MANTENEMOS la antigua
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                usuario.setPassword(usuarioAntiguo.getPassword());
            } 
            // 3. Si viene una contraseña nueva (y no es el hash antiguo), la encriptamos
            else if (!usuario.getPassword().equals(usuarioAntiguo.getPassword())) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        } 
        // 4. Es un usuario NUEVO: Obligatorio encriptar
        else {
            if (usuario.getPassword() != null) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }

        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(String run) {
        usuarioRepository.deleteById(run);
    }
}