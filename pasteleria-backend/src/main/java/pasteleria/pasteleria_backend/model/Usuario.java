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
    private String run;

    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    
    private String tipo; // 'Cliente' o 'Administrador'
    
    @Column(nullable = true) // Permitimos nulo porque Registro.tsx no lo pide
    private String pin;
    
    private String region;
    private String comuna;
    
    @Column(nullable = true) // Permitimos nulo
    private String direccion;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento; 

    @Column(name = "codigo_promo")
    private String codigoPromo;
}