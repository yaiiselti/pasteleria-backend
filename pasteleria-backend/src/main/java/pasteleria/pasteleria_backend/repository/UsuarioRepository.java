package pasteleria.pasteleria_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pasteleria.pasteleria_backend.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    // Vital para el Login: Buscar por correo
    Optional<Usuario> findByEmail(String email);
}