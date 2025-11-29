package pasteleria.pasteleria_backend.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @Column(length = 20)
    private String run; // El ID principal

    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    
    // --- CAMPOS QUE FALTABAN PARA TU FRONTEND ---
    private String tipo; // 'Cliente' o 'Administrador'
    private String pin;  // <--- VITAL para que Login.tsx funcione
    
    private String region;
    private String comuna;
    private String direccion;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento; 

    @Column(name = "codigo_promo")
    private String codigoPromo;
}