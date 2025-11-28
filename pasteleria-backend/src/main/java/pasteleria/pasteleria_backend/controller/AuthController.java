package pasteleria.pasteleria_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pasteleria.pasteleria_backend.dto.AuthResponse;
import pasteleria.pasteleria_backend.dto.LoginRequest;
import pasteleria.pasteleria_backend.model.Usuario;
import pasteleria.pasteleria_backend.repository.UsuarioRepository;
import pasteleria.pasteleria_backend.security.JwtUtil;
import pasteleria.pasteleria_backend.service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService; // <--- Usamos el servicio para guardar seguro

    // POST http://localhost:8085/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail()).orElseThrow();

        return ResponseEntity.ok(new AuthResponse(jwt, usuario.getNombre(), usuario.getTipo()));
    }

    // --- NUEVO MÉTODO: REGISTRO ---
    // POST http://localhost:8085/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        // 1. Validar si el correo ya existe
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El correo ya está registrado.");
        }

        // 2. Validar si el RUT ya existe (opcional, pero recomendado)
        if (usuarioRepository.existsById(usuario.getRun())) {
            return ResponseEntity.badRequest().body("Error: El RUT ya está registrado.");
        }

        // 3. Asignar rol por defecto si no viene
        if (usuario.getTipo() == null || usuario.getTipo().isEmpty()) {
            usuario.setTipo("Cliente"); 
        }

        // 4. Guardar usando el servicio (que ya encripta la contraseña automáticamente)
        Usuario nuevoUsuario = usuarioService.saveUsuario(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }
}