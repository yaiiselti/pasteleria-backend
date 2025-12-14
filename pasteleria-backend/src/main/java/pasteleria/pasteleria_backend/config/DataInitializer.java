package pasteleria.pasteleria_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import pasteleria.pasteleria_backend.model.Usuario;
import pasteleria.pasteleria_backend.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            String adminRun = "11.111.111-1";
            
            if (!usuarioRepository.existsById(adminRun)) {
                Usuario admin = new Usuario();
                admin.setRun(adminRun);
                admin.setNombre("Administrador");
                admin.setApellidos("Principal");
                admin.setEmail("admin@duoc.cl");
                admin.setPassword(passwordEncoder.encode("admin123")); // Contraseña inicial
                admin.setTipo("Administrador");
                admin.setRegion("Metropolitana");
                admin.setComuna("Santiago");
                admin.setPin("1234"); // PIN inicial para pruebas
                
                usuarioRepository.save(admin);
                System.out.println("✅ USUARIO ADMIN CREADO: admin@duoc.cl / admin123 / PIN: 1234");
            }
        };
    }
}