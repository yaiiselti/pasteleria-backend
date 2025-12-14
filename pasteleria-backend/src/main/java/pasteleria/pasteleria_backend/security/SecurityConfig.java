package pasteleria.pasteleria_backend.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; 
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            .authorizeHttpRequests(auth -> auth
                // 1. SWAGGER (Documentación)
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // 2. AUTENTICACIÓN PÚBLICA
                .requestMatchers("/api/auth/**").permitAll()
                
                // 3. ZONA PÚBLICA (Solo Lectura)
                // Catálogo de productos y reseñas visibles para todos
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/resenas/**").permitAll() // Ver reseñas es público
                
                // 4. ZONA DE CLIENTES (Requiere estar logueado)
                // Enviar mensajes (Contacto)
                .requestMatchers(HttpMethod.POST, "/api/mensajes/**").authenticated() 
                // Crear reseñas (Solo clientes reales)
                .requestMatchers(HttpMethod.POST, "/api/resenas/**").authenticated()
                // Gestión de Pedidos propios
                .requestMatchers(HttpMethod.POST, "/api/pedidos/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/pedidos/mis-pedidos").authenticated()

                // 5. ZONA ADMINISTRATIVA (Blindada para ROLE_ADMIN)
                // Gestión de Mensajes (Leer todo, marcar leído, borrar) <-- NUEVO BLINDAJE
                .requestMatchers(HttpMethod.GET, "/api/mensajes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/mensajes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/mensajes/**").hasRole("ADMIN")
                
                // Gestión de Reseñas (Borrar spam) <-- NUEVO BLINDAJE
                .requestMatchers(HttpMethod.DELETE, "/api/resenas/**").hasRole("ADMIN")

                // Gestión de Productos (CRUD completo)
                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                
                // Gestión Global de Usuarios y Pedidos
                .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/api/pedidos/**").hasRole("ADMIN") 

                // 6. CUALQUIER OTRA RUTA (Por defecto cerrada)
                .anyRequest().authenticated()
            )
            
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Ajusta esto al puerto exacto de tu Frontend
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}