package pasteleria.pasteleria_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String nombre; // Para mostrar "Hola, Juan" en el front
    private String rol;    // Para saber si mostrar el panel admin
}