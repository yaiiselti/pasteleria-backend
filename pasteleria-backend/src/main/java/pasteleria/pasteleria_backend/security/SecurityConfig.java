package pasteleria.pasteleria_backend.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // 1. ENCRIPTADOR DE CONTRASEÑAS (BCrypt)
    // Esto hace que "1234" se guarde como "$2a$10$r7..." en la BD.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. GESTOR DE AUTENTICACIÓN
    // Permite que Spring maneje el login automáticamente.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 3. REGLAS DE SEGURIDAD (El semáforo)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactivamos CSRF porque usamos Tokens (es seguro así)
            .csrf(csrf -> csrf.disable())
            // Activamos CORS (para que React se conecte)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // DEFINIMOS PERMISOS
            .authorizeHttpRequests(auth -> auth
                // RUTAS PÚBLICAS (Cualquiera puede entrar)
                .requestMatchers("/api/auth/**").permitAll() // Login y Registro
                .requestMatchers("/api/productos/**").permitAll() // Ver catálogo
                // RUTAS PRIVADAS (Solo con Token)
                .anyRequest().authenticated()
            )
            
            // NO USAMOS SESIONES (Stateless)
            // Como usamos Tokens, el servidor no guarda memoria de sesión.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Añadimos nuestro filtro JWT antes del filtro estándar de usuario/clave
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 4. CONFIGURACIÓN CORS (Permitir a React entrar)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permitimos el puerto de tu Frontend (Vite usa el 5173 por defecto)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
