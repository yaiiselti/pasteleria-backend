package pasteleria.pasteleria_backend.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pasteleria.pasteleria_backend.model.Usuario;
import pasteleria.pasteleria_backend.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // --- CORRECCIÓN DE SEGURIDAD: Mapeo Estándar de Roles ---
        // Normalizamos el rol para que Spring Security siempre reciba ROLE_ADMIN o ROLE_CLIENTE
        // independientemente de cómo esté escrito en la base de datos ("Administrador", "admin", etc.)
        String rolSpring;
        String tipoBD = usuario.getTipo() != null ? usuario.getTipo().trim() : "";

        if ("Administrador".equalsIgnoreCase(tipoBD) || "ADMIN".equalsIgnoreCase(tipoBD)) {
            rolSpring = "ROLE_ADMIN";
        } else {
            // Por defecto, ante la duda o si es null, lo tratamos con el rol de menor privilegio
            rolSpring = "ROLE_CLIENTE";
        }

        return new User(
                usuario.getEmail(), 
                usuario.getPassword(), 
                Collections.singletonList(new SimpleGrantedAuthority(rolSpring))
        );
    }
}