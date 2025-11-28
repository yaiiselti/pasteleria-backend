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
        // 1. Buscamos al usuario por su email en TU base de datos
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // 2. Convertimos el rol (ej: "Administrador") a un formato que Spring entienda ("ROLE_ADMIN")
        String rolSpring = "ROLE_" + usuario.getTipo().toUpperCase(); 

        // 3. Retornamos el objeto User oficial de Spring Security
        return new User(
                usuario.getEmail(), 
                usuario.getPassword(), 
                Collections.singletonList(new SimpleGrantedAuthority(rolSpring))
        );
    }
}