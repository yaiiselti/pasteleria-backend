package pasteleria.pasteleria_backend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ClienteData {
    private String nombre;
    private String email;
    private String direccion;
    private String comuna;
    private String region; // Agregamos región por si acaso
    private String medioPago;
    private String tarjeta; // Para guardar los últimos dígitos si quieres
    private String comprobante; // Para transferencias
}