package pasteleria.pasteleria_backend.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data; // Importante

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String run;
    private String nombre;
    private String apellidos;
    private String email;
    private String rol;
    private String region;
    private String comuna;
    private String direccion;
    
    // --- ESTOS FALTABAN PARA LOS BENEFICIOS ---
    private LocalDate fechaNacimiento;
    private String codigoPromo;
}