package pasteleria.pasteleria_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClienteData {
    private String nombre;
    private String email;
    private String direccion;
    private String comuna;
    private String region;
    private String medioPago;
    private String tarjeta;
    private String comprobante;
    
    // --- NUEVO CAMPO NECESARIO PARA VALIDACIÓN ---
    private String codigoPromo; 
    
    @jakarta.persistence.Transient
    private String fechaEntrega; 
}