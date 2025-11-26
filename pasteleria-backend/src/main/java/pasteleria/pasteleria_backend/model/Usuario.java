package pasteleria.pasteleria_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @Column(length = 20)
    private String run;

    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String tipo; // 'Cliente' o 'Administrador'

    // Datos de Dirección
    private String region;
    private String comuna;
    private String direccion; // <--- AGREGADO PARA QUE NO SE PIERDA

    // Datos de Beneficios
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento; // Java convierte el string "YYYY-MM-DD" automático

    @Column(name = "codigo_promo")
    private String codigoPromo;
}